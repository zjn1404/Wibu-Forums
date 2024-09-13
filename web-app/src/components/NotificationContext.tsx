import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  ReactNode,
  ReactElement,
} from "react";
import { connectSocketServer } from "../services/NotificationService";
import { Notification } from "../entity/Notification";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert"; // Import MUI's Alert component for better styling

interface NotificationContextProps {
  notifications: Notification[];
  showNotification: (message: string) => void;
}

const NotificationContext = createContext<NotificationContextProps | undefined>(
  undefined
);

export const NotificationProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  // Function to handle incoming notifications
  const onNotificationReceived = (notification: Notification) => {
    console.log("Notification received:", notification);
    setNotifications((prev) => [...prev, notification]);
    showNotification(notification.body);
  };

  // Show snackbar or toast
  const showNotification = (message: string) => {
    setSnackbarMessage(message);
    setSnackbarOpen(true); // Show snackbar
  };

  // Connect to the WebSocket server when the component mounts
  useEffect(() => {
    const socketConnection = connectSocketServer(onNotificationReceived);
    console.log("WebSocket connection established");

    return () => {
      socketConnection.disconnect(() => console.log("WebSocket disconnected"));
    };
  }, []);

  // Close snackbar handler
  const handleCloseSnackbar = () => {
    setSnackbarOpen(false);
  };

  return (
    <NotificationContext.Provider value={{ notifications, showNotification }}>
      {children}
      {/* Snackbar component for notifications */}
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={6000} // Duration in milliseconds
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity="info"
          sx={{ width: "100%" }}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </NotificationContext.Provider>
  );
};

export const useNotification = (): NotificationContextProps => {
  const context = useContext(NotificationContext);
  if (context === undefined) {
    throw new Error(
      "useNotification must be used within a NotificationProvider"
    );
  }
  return context;
};
