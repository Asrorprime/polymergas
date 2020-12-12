// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import {model} from 'utils/model'
// @ts-ignore
import api from 'api'
// @ts-ignore
import 'react-toastify/dist/ReactToastify.css';
import {toast} from "react-toastify";

const {deleteMainColor, uploadFile, saveProduct, getProduct,deleteProduct} = api;

export default modelExtend(model, {
  namespace: 'product',
  state: {
    photosId: '',
    products: [],
    totalElements: null,
  },
  subscriptions: {
    setup({dispatch, history}: { dispatch: any, history: any }) {
      history.listen((location: any) => {

      })
    }
  },
  effects: {


    * uploadAvatar({payload}: { payload: any }, {call, put, select}: any) {
      yield put({
        type: "app/updateState",
        payload: {
          loading: true
        }
      });
      const res = yield call(uploadFile, {payload});
      yield put({
        type: "app/updateState",
        payload: {
          loading: false
        }
      });
      if (res.success) {
        toast.success("Rasm saqlandi!")
        yield put({
          type: 'updateState',
          payload: {
            photosId: res.object[0].fileId,
          }
        });
        return res.object[0].fileId;
      } else {
        toast.error("Rasm saqlashda xatolik yuz berdi!")
      }
    },
  }
})
