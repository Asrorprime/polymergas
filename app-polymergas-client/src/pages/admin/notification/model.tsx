// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import {model} from 'utils/model'
// @ts-ignore
import api from 'api'
// @ts-ignore
import {toast} from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';

const {getNotification, getUserUnreadNotifications,
  readNotification,deleteNotification,getNotifications} = api;

export default modelExtend(model, {
  namespace: 'notification',
  state: {
    pathname: "",
    notifications:[],
    paginations: [],
    page: 0,
    loading:false,
    size: 10,
    currentNotification: {},
    modalShow:false,
    calledFunc:undefined,
    index:undefined,
    totalElements:0
  },
  subscriptions: {
    setup({dispatch, history}:{dispatch:any, history:any}) {
      history.listen((location:any) => {

      })
    }
  },
  effects: {
    * getNotifications({payload}:{ payload: any }, {call, put, select}:any) {
      const res = yield call(getNotifications, payload);
      yield put({
        type: "app/updateState",
        payload: {
          loading: true
        }
      });
      yield put({
        type: "updateState",
        payload: {
          notifications: res.object.content,
          totalElements: res.object.totalElements
        }
      });
      return res;
    },
    * getUserUnreadNotifications({payload}:any, {call, put, select}:any) {
      const res = yield call(getUserUnreadNotifications, payload);
      const {notifications}=yield select((_: { notification: any; })=>_.notification)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            notifications: notifications.concat(res.object.content),
            page: res.currentPage,
            // paginations: pagination(res.object.currentPage, res.object.totalPages),
            notificationsTotalElements:res.totalElements
          }
        })
      }
      yield put({
        type: 'updateState',
        payload: {
          loading: false
        }
      })
      return res;
    },
    * getNotification({payload}:{payload: {
      message:string, seen:boolean, userId: string,
      }}, {call, put, select}:any) {
      const res = yield call(getNotification, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            currentNotification: res.object
          }
        })
      }
      return res;
    },
    * readNotification({payload}:any, {call, put, select}:any) {
      const res = yield call(readNotification, payload);
      if (res.success) {
        toast.success(res.message)
      } else {
        toast.error(res.message)
      }
      return res;
    },
    * deleteNotification({payload}:any, {call, put, select}:any) {
      const res = yield call(deleteNotification, payload);
      if (res.success) {
        toast.success(res.message)
      } else {
        toast.error(res.message)
      }
      return res;
    },
  }})
