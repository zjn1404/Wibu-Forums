import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import { Login } from "../pages/Login";
import { Home } from "../pages/Home";
import Authenticate from "../components/Authenticate";
import { Register } from "../pages/Register";
import { RegisterSuccess } from "../pages/RegisterSuccess";
import { VerificationCodeExpired } from "../pages/VerificationCodeExpired";
import { Profile } from "../pages/Profile";
import { Account } from "../pages/Account";

export const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" Component={Home} />
        <Route path="/authenticate" Component={Authenticate} />
        <Route path="/login" Component={Login} />
        <Route path="/register" Component={Register} />
        <Route path="/register-success" Component={RegisterSuccess} />
        <Route path="/verification-code-expired" Component={VerificationCodeExpired} />
        <Route path="/profile" Component={Profile} />
        <Route path="/account" Component={Account} />
      </Routes>
    </Router>
  );
};
