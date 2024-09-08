import { Comment } from "./Comment";

export interface Post {
  id: string;
  avatarUrl: string;
  userId: string;
  formattedPostedDate: string;
  postedDate: string;
  content: string;
  images: string[];
  comments: Comment[];
}