import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import { Login } from "../pages/Login";
import { Home } from "../pages/Home";
import Authenticate from "../components/Authenticate";
import { Register } from "../pages/Register";
import { RegisterSuccess } from "../pages/RegisterSuccess";
import { VerificationCodeExpired } from "../pages/VerificationCodeExpired";
import { Profile } from "../pages/Profile";
import { Account } from "../pages/Account";
import { MyPost } from "../pages/MyPost";
import { ReadOnlyProfile } from "../pages/ReadOnlyProfile";
import { UserPosts } from "../pages/UserPosts";
import { UserFeatures } from "../pages/UserFeatures";
import { Friends } from "../pages/Friends";
import { Notifications } from "../pages/Notifications";
import { SpecificPost } from "../pages/SpecificPost";

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
        <Route path="/profile/:userId" Component={ReadOnlyProfile} />
        <Route path="/account" Component={Account} />
        <Route path="/my-posts" Component={MyPost} />
        <Route path="/post/:id" Component={SpecificPost} />
        <Route path="/post/user/:userId" Component={UserPosts} />
        <Route path="/user/:userId" Component={UserFeatures} />
        <Route path="/friends" Component={Friends} />
        <Route path="/notifications" Component={Notifications} />
      </Routes>
    </Router>
  );
};
