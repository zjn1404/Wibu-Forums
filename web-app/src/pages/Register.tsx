import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Snackbar, Alert, AlertColor } from "@mui/material";
import { register } from "../services/AuthenticationService";
import { CODE } from "../configurations/Configuration";

export const Register: React.FC = () => {

  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
    phoneNumber: "",
    firstName: "",
    lastName: "",
    address: "",
    dob: "",
  });

  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [snackBarType, setSnackBarType] = useState<AlertColor>("error");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleCloseSnackBar = (event?: any, reason?: any) => {
    if (reason === "clickaway") return;
    setSnackBarOpen(false);
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    // Perform validation
    if (
      formData.username === "" ||
      formData.password === "" ||
      formData.email === "" ||
      formData.dob === ""
    ) {
      setSnackBarMessage("Please fill in required information");
      setSnackBarOpen(true);
      setSnackBarType("error");
      return;
    }

    try {
      const response = await register(
        formData.username,
        formData.password,
        formData.email,
        formData.phoneNumber,
        formData.firstName,
        formData.lastName,
        formData.address,
        new Date(Date.parse(formData.dob))
      );

      setSnackBarMessage("A verification code has been sent to your email.");
      setSnackBarOpen(true);
      setSnackBarType("success");
    } catch (error: any) {
      const errorMessage =
        error?.response?.data?.message ||
        "Registration failed. Please try again.";

      setSnackBarMessage(errorMessage);
      setSnackBarOpen(true);
      setSnackBarType('error')
    }
  };

  return (
    <>
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

      <div className="d-flex align-items-center justify-content-center vh-100 bg-dark">
        <div className="card p-4 shadow" style={{ width: "50%" }}>
          <h3 className="card-title text-center mb-4">Register</h3>

          {/* Registration Form */}
          <form onSubmit={handleSubmit}>
            <div className="row">
              <div className="col-md-6 mb-3">
                <label htmlFor="username" className="form-label">
                  Username<span style={{ color: "red" }}>*</span>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="username"
                  name="username"
                  placeholder="Enter username"
                  value={formData.username}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="password" className="form-label">
                  Password<span style={{ color: "red" }}>*</span>
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="password"
                  name="password"
                  placeholder="Enter password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 mb-3">
                <label htmlFor="email" className="form-label">
                  Email<span style={{ color: "red" }}>*</span>
                </label>
                <input
                  type="email"
                  className="form-control"
                  id="email"
                  name="email"
                  placeholder="Enter email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="phoneNumber" className="form-label">
                  Phone Number
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="phoneNumber"
                  name="phoneNumber"
                  placeholder="Enter phone number"
                  value={formData.phoneNumber}
                  onChange={handleChange}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 mb-3">
                <label htmlFor="firstName" className="form-label">
                  First Name<span style={{ color: "red" }}>*</span>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="firstName"
                  name="firstName"
                  placeholder="Enter first name"
                  value={formData.firstName}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="lastName" className="form-label">
                  Last Name<span style={{ color: "red" }}>*</span>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="lastName"
                  name="lastName"
                  placeholder="Enter last name"
                  value={formData.lastName}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 mb-3">
                <label htmlFor="address" className="form-label">
                  Address
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="address"
                  name="address"
                  placeholder="Enter address"
                  value={formData.address}
                  onChange={handleChange}
                />
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="dob" className="form-label">
                  Date of Birth<span style={{ color: "red" }}>*</span>
                </label>
                <input
                  type="date"
                  className="form-control"
                  id="dob"
                  name="dob"
                  value={formData.dob}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>
            <button type="submit" className="mt-3 btn btn-dark w-100">
              Register
            </button>
          </form>

          {/* Login Link */}
          <div className="mt-4 text-center">
            <Link to="/login" className="text-dark">
              Already have an account? Log In
            </Link>
          </div>
        </div>
      </div>
    </>
  );
};
