import { DisplayPosts } from "../components/DisplayPosts";
import { getPostById } from "../services/PostService";

export const SpecificPost = () => {
  const postId = window.location.pathname.split("/")[2];

  const getPost = () => {
    return getPostById(postId);
  }
  return (
    <>
      <DisplayPosts getPosts={getPost}/>
    </>
  );
};
