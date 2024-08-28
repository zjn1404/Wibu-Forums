import { Link, useNavigate } from "react-router-dom";
import { Header } from "../components/Header";
import { useEffect, useState } from "react";
import { getMyInfo } from "../services/UserService";
import { isAuthenticated } from "../services/AuthenticationService";

export const Home: React.FC = () => {

  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState({
    username: "",
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: ""
  });

  const getUserDetails = async () => {
    try {
      const response = await getMyInfo();
      const data = response.data;

      console.log(data);

      setUserDetails(data.result);
    } catch (error) {}
  };

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
    } else {
      getUserDetails();
    }
  }, [navigate]);


  return (
    <div>
      {/* Header */}
      <Header user={userDetails} />

      {/* Main content */}
      <div className="container mt-5">
        <div className="row">
          <div className="col-12">
            <h1>Welcome to Home Page</h1>
            <p>Hello, {userDetails?.username || 'Guest'}! This is your home page.</p>
          </div>
        </div>

        {/* Additional sections if needed */}
        {/* For example, a section to display user-specific content */}
        <div className="row mt-4">
          <div className="col-md-6">
            <div className="card">
              <div className="card-body">
                <h5 className="card-title">Profile</h5>
                <p className="card-text">
                  Username: {userDetails.username || 'N/A'}
                </p>
                <p className="card-text">
                  Email: {userDetails.email || 'N/A'}
                </p>
                <Link to="/profile" className="btn btn-dark">View Profile</Link>
              </div>
            </div>
          </div>

          {/* Add more content cards or sections as necessary */}
        </div>
      </div>
    </div>
  );
}