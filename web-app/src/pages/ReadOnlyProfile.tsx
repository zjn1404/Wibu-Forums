import React, { useEffect, useState } from "react";
import { getProfileById } from "../services/ProfileService";
import { Header } from "../components/Header";
import { UserProfile } from "../entity/UserProfile";

export const ReadOnlyProfile: React.FC = () => {
  const [profile, setProfile] = useState<UserProfile>({} as UserProfile);
  const userId = window.location.pathname.split("/")[2];

  useEffect(() => {
    const fetchProfile = async () => {
      const response = await getProfileById(userId);
      setProfile(response.data.result);
    };
    fetchProfile();
  }, []);

  return (
    <div>
      <Header user={profile} />
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-md-6">
              <div className="card">
                <div className="card-body">
                  <h4 className="card-title text-center">Profile</h4>
                  <div className="mb-3">
                    <label className="form-label">User Id</label>
                    <p className="form-control">{profile?.userId}</p>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">First Name</label>
                    <p className="form-control">{profile.firstName}</p>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Last Name</label>
                    <p className="form-control">{profile.lastName}</p>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Date of Birth</label>
                    <p className="form-control">{profile?.dob ? profile.dob.split("T")[0] : ""}</p>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Address</label>
                    <p className="form-control">{profile.address}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
