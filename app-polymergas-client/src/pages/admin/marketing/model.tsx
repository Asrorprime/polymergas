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
const {saveMarketing, getMarketing, deleteMarketing} = api


export default modelExtend(model, {
    namespace: "marketing",
    totalElements: null,
    state: {
        marketings: [],

    },
    subscriptions: {},
    effects: {
        * saveMarketing({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(saveMarketing, payload);
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
        * getMarketing({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(getMarketing, payload);
            if (res.success) {
                yield put({
                    type: "updateState",
                    payload: {
                        marketings: res.object,
                        // totalElements: res.object.totalElements
                    }
                });
            }
            return res
        },
        * deleteMarketing({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(deleteMarketing, payload);
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
    }
})
