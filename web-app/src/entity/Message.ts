export interface Message {
  id: string,

  chatId: string,

  senderId: string,

  recipientId: string,

  readStatus: boolean,

  images: string[],

  attachments: string[],

  content: string,

  formatedSentDate: string
}