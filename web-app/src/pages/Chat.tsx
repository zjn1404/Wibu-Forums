import React, { useState, useEffect, useRef, useCallback } from "react";
import { IMessage, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { getFriends } from "../services/ProfileService";
import { UserProfile } from "../entity/UserProfile";
import { getMessage } from "../services/ChatService";
import { Message } from "../entity/Message";
import { getProfileFromLocalStorage } from "../services/LocalStorageService";
import { getAccessToken } from "../services/LocalStorageService";
import { API } from "../configurations/Configuration";
import { Header } from "../components/Header";

export const Chat: React.FC = () => {
  const userProfile: UserProfile = getProfileFromLocalStorage();
  const [friends, setFriends] = useState<UserProfile[]>([]);
  const [page, setPage] = useState(1);
  const [totalMessagePage, setTotalMessagePage] = useState(1);
  const [totalFriendsPage, setTotalFriendsPage] = useState(1);
  const [messagePage, setMessagePage] = useState(1);
  const [selectedFriend, setSelectedFriend] = useState<string | null>(null);
  const [messages, setMessages] = useState<Message[]>([]);
  const [newMessage, setNewMessage] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  const [hasMore, setHasMore] = useState<boolean>(true);
  const [isStompConnected, setIsStompConnected] = useState<boolean>(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const chatContainerRef = useRef<HTMLDivElement>(null);
  const stompClientRef = useRef<any>(null);

  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  const handleMessage = (message: IMessage) => {
    const newMessage = JSON.parse(message.body);
    if (
      selectedFriend === newMessage.senderId ||
      selectedFriend === newMessage.recipientId
    ) {
      setMessages((prevMessages) => {
        return [newMessage, ...prevMessages];
      });
    }
  };

  useEffect(() => {
    const socket = new SockJS(`http://localhost:8084${API.MESSAGE_SOCKET}`);
    stompClientRef.current = Stomp.over(socket);

    stompClientRef.current.connect(
      { Authorization: `Bearer ${getAccessToken()}` },
      (frame: any) => {
        console.log("WebSocket connected:", frame);
        setIsStompConnected(true);

        stompClientRef.current.subscribe(
          `/user/${userProfile.userId}/queue/messages`,
          handleMessage
        );
      },
      (error: any) => {
        console.error("STOMP client error:", error);
      }
    );

    return () => {
      stompClientRef.current.disconnect(() => {
        console.log("STOMP client disconnected");
        setIsStompConnected(false);
      });
    };
  }, [selectedFriend, userProfile.userId]);

  const fetchMessages = async (friendId: string) => {
    try {
      const response = await getMessage(messagePage, friendId);
      const newMessages = response.data.result.data;
      setTotalMessagePage(response.data.result.totalPages);

      setMessages((prevMessages) => {
        const existingIds = new Set(prevMessages.map((msg) => msg.id));
        const uniqueNewMessages = newMessages.filter(
          (msg: Message) => !existingIds.has(msg.id)
        );
        return [...uniqueNewMessages, ...prevMessages];
      });
      setMessagePage((prevPage) => prevPage + 1);
      if (newMessages.length === 0) {
        setHasMore(false);
      }
    } catch (error) {
      console.error("Error fetching messages", error);
    }
  };

  const fetchFriends = async () => {
    setLoading(true);
    try {
      const res = await getFriends(page);
      setTotalFriendsPage(res.data.result.totalPages);

      setFriends((prev) => {
        const existingIds = new Set(prev.map((friend) => friend.userId));
        const uniqueNewFriends = res.data.result.data.filter(
          (friend: UserProfile) => !existingIds.has(friend.userId)
        );
        return [...prev, ...uniqueNewFriends];
      });

      setPage((prev) => prev + 1);
    } catch (error) {
      console.error("Error fetching friends", error);
    } finally {
      setLoading(false);
    }
  };

  const handleChatScroll = () => {
    if (chatContainerRef.current) {
      const top = chatContainerRef.current.scrollTop === 0;
      if (top && hasMore && !loading) {
        if (selectedFriend && messagePage <= totalMessagePage) {
          fetchMessages(selectedFriend);
        }
      }
    }
  };

  const handleFriendClick = (friendId: string) => {
    if (friendId !== selectedFriend) {
      setSelectedFriend(friendId);
      setMessages([]);
      setMessagePage(1);
      setHasMore(true);
      fetchMessages(friendId);
    }
  };

  const handleSendMessage = () => {
    if (newMessage.trim() && selectedFriend && isStompConnected) {
      const messageToSend = {
        content: newMessage,
        senderId: userProfile.userId,
        senderName: userProfile.firstName + " " + userProfile.lastName,
        recipientId: selectedFriend,
      };

      const headers = {
        Authorization: `Bearer ${getAccessToken()}`,
      };

      stompClientRef.current.send(
        "/app/chat",
        headers,
        JSON.stringify(messageToSend)
      );

      setNewMessage("");
    } else {
      console.error("Cannot send message, STOMP client is not connected.");
    }
  };

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setNewMessage(event.target.value);
  };

  const loadMoreFriends = useCallback(() => {
    if (hasMore) {
      fetchFriends();
    }
  }, [hasMore]);

  const handleScroll = () => {
    if (chatContainerRef.current) {
      const bottom =
        chatContainerRef.current.scrollHeight ===
        chatContainerRef.current.scrollTop +
          chatContainerRef.current.clientHeight;
      if (bottom && hasMore && !loading) {
        loadMoreFriends();
      }
    }
  };

  useEffect(() => {
    fetchFriends();
  }, []);

  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  return (
    <>
      <Header user={userProfile} />
      <div className="chat-page">
        <div className="sidebar">
          <h2>Friends</h2>
          <div
            className="friends-list"
            ref={chatContainerRef}
            onScroll={handleScroll}
          >
            {friends.map((friend) => (
              <button
                key={friend.userId}
                className={`friend ${
                  selectedFriend === friend.userId ? "selected" : ""
                }`}
                onClick={() => handleFriendClick(friend.userId)}
              >
                {friend.firstName + " " + friend.lastName}
              </button>
            ))}
            {loading && <div className="loading">Loading...</div>}
          </div>
        </div>
        <div className="chat-window">
          {selectedFriend ? (
            <>
              <div
                className="messages"
                ref={chatContainerRef}
                onScroll={handleChatScroll}
              >
                {messages.map((message) => (
                  <div
                    key={message.id}
                    className={`message ${
                      message.senderId === userProfile.userId
                        ? "my-message"
                        : "other-message"
                    }`}
                  >
                    <div className="message-content">{message.content}</div>
                    <div className="message-time">
                      {message.formatedSentDate}
                    </div>
                  </div>
                ))}
                <div ref={messagesEndRef} />
              </div>
              <div className="chat-input">
                <input
                  type="text"
                  value={newMessage}
                  onChange={handleInputChange}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      handleSendMessage();
                    }
                  }}
                  placeholder="Type a message"
                />
                <button onClick={handleSendMessage}>Send</button>
              </div>
            </>
          ) : (
            <div className="no-chat">Select a friend to start chatting</div>
          )}
        </div>
      </div>
    </>
  );
};
