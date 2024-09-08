import React, { useEffect, useState } from "react";
import { getMyInfo, updateMyInfo } from "../services/UserService";
import { Alert, Snackbar } from "@mui/material";
import { Header } from "../components/Header";
import { changePassword } from "../services/AuthenticationService";
import { sendVerificationCode } from "../services/VerifyService";
import { UserProfile } from "../entity/UserProfile";
import { getProfileFromLocalStorage } from "../services/LocalStorageService"; 

export const Account: React.FC = () => {
  const [account, setAccount] = useState({
    email: "",
    phoneNumber: "",
    currentPassword: "",
    newPassword: "",
  });

  const [profile, setProfile] = useState<UserProfile>({} as UserProfile);
  const [verificationCode, setVerificationCode] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [showVerificationButton, setShowVerificationButton] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarType, setSnackBarType] = useState<"success" | "error">(
    "success"
  );

  useEffect(() => {
    setProfile(getProfileFromLocalStorage())
  }, []);
  
  const handleCloseSnackBar = (event?: any, reason?: any) => {
    if (reason === "clickaway") return;
    setSnackBarOpen(false);
  };

  const getAccount = async () => {
    try {
      const response = await getMyInfo();
      const data = response.data;
      setAccount({
        ...account,
        email: data.result.email,
        phoneNumber: data.result.phoneNumber,
      });
    } catch (error) {
      console.error("Failed to load profile", error);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setAccount({
      ...account,
      [name]: value,
    });

    if (name === "verificationCode") {
      setVerificationCode(value);
    }
    if (name === "email") {
      setShowVerificationButton(true);
    }
  };

  const handleSendVerificationCode = async () => {
    setIsLoading(true);
    try {
      await sendVerificationCode(account.email);
      setSnackBarMessage("Verification code sent successfully!");
      setSnackBarType("success");
    } catch (error: any) {
      setSnackBarMessage(
        error?.response?.data?.message || "Failed to send verification code."
      );
      setSnackBarType("error");
    } finally {
      setSnackBarOpen(true);
      setIsLoading(false);
    }
  };

  const handleUpdateAccount = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();
    setIsLoading(true);

    try {
      await updateMyInfo(account.email, account.phoneNumber, verificationCode);
      setSnackBarMessage("Profile updated successfully!");
      setSnackBarType("success");
    } catch (error: any) {
      setSnackBarMessage(
        error?.response?.data?.message || "An error occurred. Please try again."
      );
      setSnackBarType("error");
    } finally {
      setSnackBarOpen(true);
      setIsLoading(false);
    }
  };

  const handleChangePassword = async (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();
    setIsLoading(true);

    try {
      await changePassword(account.currentPassword, account.newPassword);
      setSnackBarMessage("Password changed successfully!");
      setSnackBarType("success");
    } catch (error: any) {
      setSnackBarMessage(
        error?.response?.data?.message || "An error occurred. Please try again."
      );
      setSnackBarType("error");
    } finally {
      setSnackBarOpen(true);
      setIsLoading(false);
    }
  };

  useEffect(() => {
    getAccount();
  }, []);

  return (
    <div>
      <Snackbar
        open={snackBarOpen}
        onClose={handleCloseSnackBar}
        autoHideDuration={6000}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert
          onClose={handleCloseSnackBar}
          severity={snackBarType}
          variant="filled"
          sx={{ width: "100%" }}
        >
          {snackBarMessage}
        </Alert>
      </Snackbar>
      <Header user={profile}/>
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="container">
          <form onSubmit={handleUpdateAccount}>
            <div className="row">
              {/* First Column: Change Password */}
              <div className="col-md-6">
                <div className="card mb-3">
                  <div className="card-body">
                    <h4 className="card-title text-center">Change Password</h4>
                    <div className="mb-3">
                      <label className="form-label">Current Password</label>
                      <input
                        type="password"
                        name="currentPassword"
                        className="form-control"
                        value={account.currentPassword}
                        onChange={handleChange}
                      />
                    </div>
                    <div className="mb-3">
                      <label className="form-label">New Password</label>
                      <input
                        type="password"
                        name="newPassword"
                        className="form-control"
                        value={account.newPassword}
                        onChange={handleChange}
                      />
                    </div>
                    <div className="text-center">
                      <button
                        type="button"
                        onClick={handleChangePassword}
                        className="btn btn-dark"
                        disabled={isLoading}
                      >
                        {isLoading ? "Changing..." : "Change Password"}
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              {/* Second Column: Update Account */}
              <div className="col-md-6">
                <div className="card">
                  <div className="card-body">
                    <h4 className="card-title text-center">Account</h4>
                    <div className="mb-3">
                      <label className="form-label">Email</label>
                      <input
                        type="email"
                        name="email"
                        className="form-control"
                        value={account.email}
                        onChange={handleChange}
                      />
                      {showVerificationButton && (
                        <>
                          <button
                            type="button"
                            className="btn btn-link"
                            onClick={handleSendVerificationCode}
                            disabled={isLoading}
                          >
                            Send Verification Code
                          </button>
                          <input
                            type="text"
                            name="verificationCode"
                            className="form-control"
                            value={verificationCode}
                            placeholder="Verification Code"
                            onChange={handleChange}
                          />
                        </>
                      )}
                    </div>
                    <div className="mb-3">
                      <label className="form-label">Phone Number</label>
                      <input
                        type="text"
                        name="phoneNumber"
                        className="form-control"
                        value={account.phoneNumber}
                        onChange={handleChange}
                      />
                    </div>
                    <div className="text-center">
                      <button
                        type="submit"
                        className="btn btn-dark"
                        disabled={isLoading}
                      >
                        {isLoading ? "Updating..." : "Update"}
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};
