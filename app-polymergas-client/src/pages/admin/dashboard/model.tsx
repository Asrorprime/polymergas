// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import {model} from 'utils/model'
// @ts-ignore
import api from 'api'
// @ts-ignore
import {toast} from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';

const {getDashboardInfo} = api;

export default modelExtend(model, {
  namespace: 'dashboard',
  state: {
    infoForDashbord: null
  },
  subscriptions: {
    setup({dispatch, history}:{dispatch:any, history:any}) {
      history.listen((location:any) => {

      })
    }
  },
  effects: {
    *getDashboardInfo({payload}: { payload: any }, {call, put, select}: any) {
      const res = yield call(getDashboardInfo, payload);
      if (res.success) {
        yield put({
          type: "updateState",
          payload: {
            infoForDashbord: res.object
          }
        });
      }
      return res
    },
  }})
