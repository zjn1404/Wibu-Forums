import React from "react";

export const VerificationCodeExpired: React.FC = () => {
  return (
    <div className="d-flex align-items-center justify-content-center vh-100 bg-dark">
      <div className="card p-4 shadow text-center" style={{ width: "50%" }}>
        <h3 className="card-title mb-4">Verification Code Expired!</h3>
        <p className="mb-4">
          Your verification code has expired, a new one has been sent to your email.
        </p>
      </div>
    </div>
  );
};