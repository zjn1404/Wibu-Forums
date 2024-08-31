import React from "react";
import { Link } from "react-router-dom";

export const RegisterSuccess: React.FC = () => {
  return (
    <div className="d-flex align-items-center justify-content-center vh-100 bg-dark">
      <div className="card p-4 shadow text-center" style={{ width: "50%" }}>
        <h3 className="card-title mb-4">Registration Successful!</h3>
        <p className="mb-4">
          Your account has been successfully created. You can now log in and start using our services.
        </p>
        <Link to="/login" className="btn btn-dark w-100">
          Go to Login
        </Link>
      </div>
    </div>
  );
};