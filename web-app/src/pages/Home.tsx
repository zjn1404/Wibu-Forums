import { Link, useNavigate } from "react-router-dom";
import { Header } from "../components/Header";
import { useEffect, useState } from "react";
import { getMyInfo } from "../services/UserService";
import { isAuthenticated } from "../services/AuthenticationService";
import { getAccessToken } from "../services/LocalStorageService";
import { CODE } from "../configurations/Configuration";
import { Alert, Snackbar, Modal, Box, TextField, Button } from "@mui/material";

export const Home: React.FC = () => {
  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState({
    username: "",
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    noPassword: true,
  });

  const [password, setPassword] = useState("");
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [snackType, setSnackType] = useState<"success" | "error">("error");
  const [openModal, setOpenModal] = useState(true);

  const handleCloseSnackBar = (event?: any, reason?: any) => {
    if (reason === "clickaway") return;
    setSnackBarOpen(false);
  };

  const showError = (message: any) => {
    setSnackType("error");
    setSnackBarMessage(message);
    setSnackBarOpen(true);
  };

  const showSuccess = (message: any) => {
    setSnackType("success");
    setSnackBarMessage(message);
    setSnackBarOpen(true);
  };

  const getUserDetails = async () => {
    try {
      const response = await getMyInfo();
      const data = response.data;

      setUserDetails(data.result);

      if (data.result.noPassword) {
        setOpenModal(true); // Open the modal if no password is set
      }
    } catch (error) {}
  };

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
    } else {
      getUserDetails();
    }
  }, [navigate]);

  const addPassword = (event: any) => {
    event.preventDefault();

    const body = {
      password: password,
    };

    fetch("http://localhost:8888/api/identity/auth/create-password", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${getAccessToken()}`,
      },
      body: JSON.stringify(body),
    })
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        if (data.code !== CODE.SUCCESS) throw new Error(data.message);

        getUserDetails();
        showSuccess(data.message);
        setOpenModal(false); // Close modal after successful password creation
      })
      .catch((error) => {
        showError(error.message);
      });
  };

  return (
    <div>
      {/* Snackbar for notifications */}
      <Snackbar
        open={snackBarOpen}
        onClose={handleCloseSnackBar}
        autoHideDuration={6000}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert
          onClose={handleCloseSnackBar}
          severity={snackType}
          variant="filled"
          sx={{ width: "100%" }}
        >
          {snackBarMessage}
        </Alert>
      </Snackbar>

      {/* Header */}
      <Header user={userDetails} />

      {/* Modal for creating password */}
      <Modal
        open={openModal}
        onClose={(event, reason) => {
          if (reason !== "backdropClick") {
            setOpenModal(false);
          }
        }}
        disableEscapeKeyDown // Disable closing by pressing the Escape key
        aria-labelledby="modal-title"
        aria-describedby="modal-description"
      >
        <Box
          sx={{
            position: "absolute" as "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 400,
            bgcolor: "#1b1e21",
            color: "white",
            boxShadow: 24,
            p: 4,
            borderRadius: 2,
            outline: "none", // Remove blue outline on refresh
          }}
        >
          <h2 id="modal-title">Create Password</h2>
          <form onSubmit={addPassword}>
          <TextField
            label="Password"
            variant="outlined"
            type="password"
            fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            sx={{
              mt: 2,
              "& .MuiInputBase-root": {
                color: "#f7f7f7", // Text color for input
              },
              "& .MuiOutlinedInput-root": {
                "& fieldset": {
                  borderColor: "lightgrey", // Default border color
                },
                "&:hover fieldset": {
                  borderColor: "grey", // Border color on hover
                },
                "&.Mui-focused fieldset": {
                  borderColor: "lightgrey", // Border color when focused
                },
              },
              "& .MuiInputLabel-root": {
                color: "white", // Text color for the label
              },
              "& .MuiInputLabel-root.Mui-focused": {
                color: "white", // Text color for the label when focused
              },
            }}
            InputLabelProps={{
              style: { color: "white" }, // Ensures label is white
            }}
          />
          <Button
            variant="contained"
            color="inherit"
            type="submit"
            sx={{ mt: 3, width: "100%" }}
          >
            Create
          </Button>
          </form>
        </Box>
      </Modal>

      {/* Main content */}
      <div className="container mt-5">
        <div className="row">
          <div className="col-12">
            <h1>Welcome to Home Page</h1>
            <p>
              Hello, {userDetails?.username || "Guest"}! This is your home page.
            </p>
          </div>
        </div>

        {/* Additional sections if needed */}
        <div className="row mt-4">
          <div className="col-md-6">
            <div className="card">
              <div className="card-body">
                <h5 className="card-title">Profile</h5>
                <p className="card-text">
                  Username: {userDetails.username || "N/A"}
                </p>
                <p className="card-text">Email: {userDetails.email || "N/A"}</p>
                <Link to="/profile" className="btn btn-dark">
                  View Profile
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
