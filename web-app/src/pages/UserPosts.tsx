import { getPostById } from "../services/PostService";
import { DisplayPosts } from "../components/DisplayPosts";

export const UserPosts = () => {
  const userId = window.location.pathname.split("/")[2];

  const getPost = (page: number) => {
    return getPostById(userId, page);
  }

  return (
    <>
      <DisplayPosts getPosts={getPost} />
    </>
  );
};
