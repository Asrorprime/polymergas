// @ts-ignore
import {model} from '@/utils/model'
// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import api from "api"
// @ts-ignore
import {toast} from "react-toastify";
// @ts-ignore
// import {STORAGE_NAME} from "../utils/constant";
const {saveSale, getSale, deleteSale, changeIsActive,getParticipants} = api


export default modelExtend(model, {
    namespace: "sale",
    totalElements: null,
    state: {
        sales: [],
        participants:[]
    },
    subscriptions: {},
    effects: {

        * saveSale({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(saveSale, payload);
            if (res.success) {
                toast.success(res.message)
                yield put({
                    type: "updateState",
                    payload: {
                        loading: true
                    }
                });
            } else {
                toast.error(res.message)
            }
            return res
        },
        * getSale({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(getSale, payload);
            if (res.success) {
                yield put({
                    type: "updateState",
                    payload: {
                        sales: res.object.content,
                        totalElements: res.object.totalElements
                    }
                });
            }
            return res
        },
      * getParticipants({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(getParticipants, payload);
            if (res.success) {
                yield put({
                    type: "updateState",
                    payload: {
                        participants: res.object.content,
                        totalElements: res.object.totalElements
                    }
                });
            }
            return res
        },
        * deleteSale({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(deleteSale, payload);
            if (res.success) {
                toast.success(res.message)
                yield put({
                    type: "getSale",
                    payload: {
                        page: 0,
                        size: 10
                    }
                });
            } else {
                toast.error(res.message)
            }
            return res
        },
        * changeIsActive({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(changeIsActive, payload);
            if (res.success){
                toast.success(res.massage)
                return true
            }else {
                toast.error(res.massage)
                return false
            }
        },
    }
})
