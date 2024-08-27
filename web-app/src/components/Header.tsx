import React from "react";
import { Link } from "react-router-dom";
import { FaHome, FaUserFriends, FaEnvelope, FaBell } from "react-icons/fa"; 

type User = {
  user?: {
    name: string;
    avatarUrl: string;
  };
};

export const Header: React.FC<{
  user?: User;
}> = (props) => {
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

        {/* Search bar */}
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
        <div className="d-flex align-items-center mx-auto">
          <Link to="/" className="nav-link text-white me-5">
            <FaHome size={24} />
          </Link>
          <Link to="/friends" className="nav-link text-white ms-5 me-5">
            <FaUserFriends size={24} />
          </Link>
          <Link to="/messages" className="nav-link text-white ms-5 me-5">
            <FaEnvelope size={24} />
          </Link>
          <Link to="/notifications" className="nav-link text-white ms-5">
            <FaBell size={24} />
          </Link>
        </div>

        {/* Right-side content */}
        <div className="d-flex align-items-center">
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
                <Link to="/profile" className="dropdown-item">
                  Profile
                </Link>
              </li>
              <li>
                <Link to="/logout" className="dropdown-item">
                  Logout
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  );
};
