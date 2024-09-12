import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { FaHome, FaUserFriends, FaComments, FaBell } from "react-icons/fa";
import { MdGroups } from "react-icons/md";
import { logOut } from "../services/AuthenticationService";
import { UserProfile } from "../entity/UserProfile";
import { Avatar } from "@mui/material";
import { fetchUnreadNotifications } from "../services/NotificationService";

export const Header: React.FC<{
  user?: UserProfile;
}> = (props) => {
  const [hasUnreadNotifications, setHasUnreadNotifications] = useState(false);

  useEffect(() => {
    const checkUnreadNotifications = async () => {
      try {
        const unreadNotifications = await fetchUnreadNotifications();
        if (unreadNotifications && unreadNotifications.length > 0) {
          setHasUnreadNotifications(true);
        } else {
          setHasUnreadNotifications(false);
        }
      } catch (error) {
        console.error("Error fetching unread notifications:", error);
        setHasUnreadNotifications(false);
      }
    };

    // Call the function immediately to check for notifications on mount
    checkUnreadNotifications();

    // Set up an interval to periodically check for new notifications
    const interval = setInterval(checkUnreadNotifications, 5000); // Check every 5 seconds

    // Cleanup interval on component unmount
    return () => clearInterval(interval);
  }, []);

  const handleLogout = async (event: any) => {
    await logOut();
    window.location.href = "/login";
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
      <div className="container">
        <Link to="/" className="navbar-brand">
          <img
            src="/logo/logo-transparent.png"
            alt="Logo"
            width="50"
            className="d-inline-block align-text-top transparent-logo"
          />
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarContent"
          aria-controls="navbarContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarContent">
          <div className="d-flex align-items-center w-40">
            <form className="w-100 d-flex">
              <input
                className="form-control me-2"
                type="search"
                placeholder="Search"
                aria-label="Search"
              />
              <button className="btn btn-outline-light" type="submit">
                Search
              </button>
            </form>
          </div>

          <ul className="navbar-nav d-flex align-items-center mx-auto">
            <li className="nav-item">
              <Link to="/" className="nav-link text-white ms-5 me-5">
                <FaHome size={24} />
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/friends" className="nav-link text-white ms-5 me-5">
                <FaUserFriends size={24} />
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/groups" className="nav-link text-white ms-5 me-5">
                <MdGroups size={24} />
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/messages" className="nav-link text-white ms-5 me-5">
                <FaComments size={24} />
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/notifications"
                className="nav-link text-white ms-5 me-5"
              >
                <FaBell
                  size={24}
                  color={hasUnreadNotifications ? "#3194bb" : "white"}
                />
              </Link>
            </li>
          </ul>

          <div className="d-flex align-items-center justify-content-center">
            <div className="nav-item dropdown">
              <a
                className="nav-link dropdown-toggle d-flex align-items-center text-white"
                href="#"
                id="navbarDropdown"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                <Avatar
                  alt="User Avatar"
                  className="rounded-circle me-2"
                  style={{ width: "32px", height: "32px" }}
                />
                <span>
                  {props.user?.firstName + " " + props.user?.lastName}
                </span>
              </a>
              <ul
                className="dropdown-menu dropdown-menu-end"
                aria-labelledby="navbarDropdown"
              >
                <li>
                  <Link to="/profile" className="dropdown-item btn btn-dark">
                    Profile
                  </Link>
                </li>
                <li>
                  <Link to="/my-posts" className="dropdown-item btn btn-dark">
                    My Posts
                  </Link>
                </li>
                <li>
                  <Link to="/account" className="dropdown-item btn btn-dark">
                    Account
                  </Link>
                </li>
                <li>
                  <button
                    className="dropdown-item btn btn-dark"
                    onClick={handleLogout}
                  >
                    Logout
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
};
