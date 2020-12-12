// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import {model} from 'utils/model'
// @ts-ignore
import api from 'api'
// @ts-ignore
import {toast} from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';

const {} = api;

export default modelExtend(model, {
  namespace: '',
  state: {

  },
  subscriptions: {
    setup({dispatch, history}:{dispatch:any, history:any}) {
      history.listen((location:any) => {

      })
    }
  },
  effects: {

  }})
