import React, { useEffect, useState } from "react";
import { updateProfile } from "../services/ProfileService";
import { getProfileFromLocalStorage } from "../services/LocalStorageService"; 
import { Alert, Snackbar } from "@mui/material";
import { Header } from "../components/Header";
import { UserProfile } from "../entity/UserProfile";

export const Profile: React.FC = () => {
  const [profile, setProfile] = useState<UserProfile>({} as UserProfile);
  const [isLoading, setIsLoading] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarType, setSnackBarType] = useState<"success" | "error">(
    "success"
  );

  const handleCloseSnackBar = (event?: any, reason?: any) => {
    if (reason === "clickaway") return;
    setSnackBarOpen(false);
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsLoading(true);

    try {
      const response = await updateProfile(
        profile?.firstName ?? "",
        profile?.lastName ?? "",
        new Date(Date.parse(profile?.dob ?? "")),
        profile?.address ?? ""
      );

      setSnackBarMessage("Profile updated successfully!");
      setSnackBarType("success");
      setSnackBarOpen(true);
      setIsLoading(false);
    } catch (error: any) {
      setSnackBarMessage(
        error?.response?.data?.message || "An error occurred. Please try again."
      );
      setSnackBarType("error");
      setSnackBarOpen(true);
      setIsLoading(false);
    }
  };

  useEffect(() => {
    setProfile(getProfileFromLocalStorage())
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setProfile({
      ...profile,
      [e.target.name]: e.target.value,
    });
  };

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
          <form onSubmit={handleSubmit}>
            <div className="row justify-content-center">
              <div className="col-md-6">
                <div className="card">
                  <div className="card-body">
                    <h4 className="card-title text-center">Profile</h4>
                    <div className="mb-3">
                      <label className="form-label">User Id</label>
                      <p className="form-control">{profile?.userId}</p>
                    </div>
                    <div className="mb-3">
                      <label className="form-label">First Name</label>
                      <input
                        type="text"
                        name="firstName"
                        className="form-control"
                        value={profile?.firstName}
                        onChange={handleChange}
                      />
                    </div>
                    <div className="mb-3">
                      <label className="form-label">Last Name</label>
                      <input
                        type="text"
                        name="lastName"
                        className="form-control"
                        value={profile?.lastName}
                        onChange={handleChange}
                      />
                    </div>
                    <div className="mb-3">
                      <label className="form-label">Date of Birth</label>
                      <input
                        type="date"
                        name="dob"
                        className="form-control"
                        value={profile?.dob ? profile.dob.split("T")[0] : ""}
                        onChange={handleChange}
                      />
                    </div>
                    <div className="mb-3">
                      <label className="form-label">Address</label>
                      <input
                        type="text"
                        name="address"
                        className="form-control"
                        value={profile?.address}
                        onChange={handleChange}
                      />
                    </div>
                    <div className="text-center">
                      <button
                        type="submit"
                        className="btn btn-dark align-item-center"
                        disabled={isLoading}
                      >
                        {isLoading ? "Updating..." : "Update Profile"}
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
