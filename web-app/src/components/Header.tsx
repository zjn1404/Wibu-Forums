import React from "react";
import { Link } from "react-router-dom";
import { FaHome, FaUserFriends, FaEnvelope, FaBell } from "react-icons/fa";
import { MdGroups } from "react-icons/md";
import { logOut } from "../services/AuthenticationService";

export const Header: React.FC<{
  user?: any;
}> = (props) => {
  const handleLogout = async (even: any) => {
    await logOut();
    window.location.href = "/login";
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
      <div className="container">
        {/* Logo */}
        <Link to="/" className="navbar-brand">
          <img
            src="/logo/logo-transparent.png"
            alt="Logo"
            width="50"
            className="d-inline-block align-text-top transparent-logo"
          />
        </Link>

        {/* Toggle button for mobile */}
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

        {/* Search bar */}
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

          {/* Icons in the middle */}
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
                <FaEnvelope size={24} />
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/notifications"
                className="nav-link text-white ms-5 me-5"
              >
                <FaBell size={24} />
              </Link>
            </li>
          </ul>

          {/* Right-side content */}
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
                <img
                  // src={props.user.avatarUrl}
                  src="/logo/logo.jpeg"
                  alt="User Avatar"
                  className="rounded-circle me-2"
                  style={{ width: "32px", height: "32px" }}
                />
                {/* <span>{props.user.name}</span> */}
                <span>User</span>
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
                  <button className="dropdown-item btn btn-dark" onClick={handleLogout}>
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
