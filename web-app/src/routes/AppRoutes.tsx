import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import { Login } from "../pages/Login";
import { Home } from "../pages/Home";
import Authenticate from "../components/Authenticate";

export const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" Component={Home} />
        <Route path="/authenticate" Component={Authenticate} />
        <Route path="/login" Component={Login} />
      </Routes>
    </Router>
  );
};
