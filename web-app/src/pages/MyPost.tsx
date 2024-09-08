import { getMyPosts } from "../services/PostService";
import { DisplayPosts } from "../components/DisplayPosts";

export const MyPost = () => {
  return (
    <>
      <DisplayPosts getPosts={getMyPosts} />
    </>
  );
};
