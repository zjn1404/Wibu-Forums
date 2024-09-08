import { getFriendsPosts } from "../services/PostService";
import { DisplayPosts } from "../components/DisplayPosts";

export const Home = () => {
  return (
    <>
      <DisplayPosts getPosts={getFriendsPosts} />
    </>
  );
};
