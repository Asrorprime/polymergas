// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import {model} from 'utils/model'
// @ts-ignore
// @ts-ignore
import api from "api"

import {toast} from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';


export default modelExtend(model, {
  namespace: 'order',
  state: {
    pathname: "",
    delModalShow: false,
    addModal: false,
    nextModal: false,
    changeModal: false,
    historyModal: false,
    currentOrder: {},
    orderIndex: false,
    deleteId: 0,
    parentId: 0,
    loading: false,
    orders: [],
    curFunc: undefined,
    state: ["CREATED", "IN_PROGRESS", "CLOSED", "CANCELLED", "REJECTED",]
  },
  subscriptions: {
    setup({dispatch, history}: { dispatch: any, history: any }) {
      history.listen((location: any) => {

      })
    }
  },
  effects: {
    * request({payload}: any, {put, call, select}: any) {
      yield put({
        type: "app/updateState",
        payload: {
          loading: true
        }
      });
      let res;
      let apiName = payload.urlApiName;
      let objectName = payload.objectName;
      payload.urlApiName = undefined;
      payload.objectName = undefined;
      res = yield call(api[apiName], payload);

      yield put({
        type: "app/updateState",
        payload: {
          loading: false
        }
      })
      if (res.success) {
        if (objectName) {
          if (res.object && res.object.content)
            yield put({
              type: "updateState",
              payload: {
                [objectName]: res.object.content,
                page: payload.page,
                totalElements: res.object.totalElements,
                size: 10
              }
            })
          else if (res.object) {
            yield put({
              type: "updateState",
              payload: {
                [objectName]: res.object,

              }
            })
          } else {
            yield put({
              type: "updateState",
              payload: {
                [objectName]: res,
              }
            })
          }
        }
        if (res.message)
          toast.success(res.message);

      } else {
        if (res.message)
          toast.error(res.message);
      }
      return res
    },
    * changeToPayed({payload}: any, {put, call, select}: any) {
      const res = yield call(api.changeToPayed, payload);
      if (res.success) {
        toast.success(res.message);
      } else {
        toast.error(res.message);
      }
      return res
    },
    * changeOrderSum({payload}: any, {put, call, select}: any) {
      const res = yield call(api.changeOrderSum, payload);
      if (res.success) {
        toast.success(res.message);
      } else {
        toast.error(res.message);
      }
      return res
    }
  }
})

