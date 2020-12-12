export const STORAGE_NAME = 'polymergas-token';
export const getMessageType = (messageType: string) => {
  if (messageType === 'CREATED') return ' Yaratilgan bosqichda';
  if (messageType === 'IN_PROGRESS') return ' Jarayonda bosqichda';
  if (messageType === 'CLOSED') return ' Yopilgan bosqichda';
  if (messageType === 'CANCELLED') return ' Bekor qilingan bosqichda';
  if (messageType === 'REJECTED') return ' Qaytarilgan bosqichda ';

};
