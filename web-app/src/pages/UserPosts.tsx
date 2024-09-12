import { getPostByUserId } from "../services/PostService";
import { DisplayPosts } from "../components/DisplayPosts";

export const UserPosts = () => {
  const userId = window.location.pathname.split("/")[3];

  const getPost = (page: number) => {
    return getPostByUserId(userId, page);
  }

  return (
    <>
      <DisplayPosts getPosts={getPost} />
    </>
  );
};
