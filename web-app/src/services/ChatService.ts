import { API } from '../configurations/Configuration';
import { HttpClient } from '../configurations/HttpClient';

export const getMessage = async (page: number, recipientId: string) => {
  return await HttpClient.get(`${API.GET_MESSAGE}`, {
    params: {
      page: page,
      size: 10,
      recipientId: recipientId
    },
    headers: {
      Authorization: `Bearer ${localStorage.getItem('accessToken')}`
    },
  });
};

export const markMessageAsRead = async (senderId: string, recipientId: string) => {
  return await HttpClient.put(`${API.MARK_MESSAGE_AS_READ}`, {
    params: {
      senderId,
      recipientId,
    },
    headers: {
      Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
    },
  });
};