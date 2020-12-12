
// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import {model} from 'utils/model'
// @ts-ignore
import api from 'api'
// @ts-ignore
import {toast} from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';
import {STORAGE_NAME} from "@/utils/constant";
import {history} from "@@/core/history";

const {editUser,editPassword,editUserName} = api;

export default modelExtend(model, {
  namespace: 'login',
  state: {
    password: '',
    oldPassword: '',
    prePassword: '',
    phoneNumber: '',
    newPhoneNumber: '',
  },
  subscriptions:  {
    setup({dispatch, history}:{dispatch:any, history:any}) {
      history.listen((location:any) => {

      })
    }
  },
  effects: {
    // * editUser({payload}:{payload:{password:string,oldPassword:string}}, {put, call, select}:any) {
    //   const res = yield call(editUser, payload);
    //   if (res.success) {
    //     localStorage.setItem(STORAGE_NAME, "benefit-token"  + " " + res.message);
    //     yield put({
    //       type:"app/userMe"
    //     })
    //     toast.success("Ma'lumot oʻzgaritrildi");
    //
    //   } else {
    //     if (res.errors) {
    //       // @ts-ignore
    //       res.errors && res.errors.map(item => {
    //         toast.error(item.defaultMessage);
    //       })
    //     } else {
    //       toast.error(res.message);
    //     }
    //   }
    //   return res;
    // },
    * editPassword({payload}:{payload:{password:string,oldPassword:string,prePassword:string}}, {put, call, select}:any) {
      const res = yield call(editPassword, payload);
      if (res.success) {
        toast.success("Muvaffaqiyatli oʻzgartirildi!");
        localStorage.removeItem(STORAGE_NAME);
        history.push("/");
      } else {
        if (res.errors) {
          // @ts-ignore
          res.errors && res.errors.map(item => {
            toast.error(item.defaultMessage);
          })
        } else {
          toast.error(res.message);
        }
      }
      return res;
    },
    * editUserName({payload}:{payload:{phoneNumber:string,newPhoneNumber:string,oldPassword:string}}, {put, call, select}:any) {
      const res = yield call(editUserName, payload);
      if (res.success) {
        toast.success("Muvaffaqiyatli oʻzgartirildi!");
        localStorage.removeItem(STORAGE_NAME);
        history.push("/");
      } else {
        if (res.errors) {
          // @ts-ignore
          res.errors && res.errors.map(item => {
            toast.error(item.defaultMessage);
          })
        } else {
          toast.error(res.message);
        }
      }
      return res;
    },
  }})
