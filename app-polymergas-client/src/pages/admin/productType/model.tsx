// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import {model} from '@/utils/model'
// @ts-ignore
import api from 'api'
// @ts-ignore
import {toast} from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';

const {deleteProductType, saveProductType, getProductTypes, getSearchedProductTypes} = api;

export default modelExtend(model, {
    namespace: 'productType',
    state: {
        totalElements: 0,
        productTypes: [],
        searchedProductTypes: [],
    },
    subscriptions: {
        setup({dispatch, history}: { dispatch: any, history: any }) {
            history.listen((location: any) => {

            })
        }
    },
    effects: {
        * saveProductType({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(saveProductType, payload);
            yield put({
                type: "app/updateState",
                payload: {
                    loading: true
                }
            });
            if (res.success) {
                toast.success('Saqlandi')
                yield put({
                    type: "getProductTypes",
                    payload: {
                        page: 0,
                        size: 10
                    }
                });
            } else {
                toast.error('Xatolik yuz berdi !')
            }
            return res
        },
        * getProductTypes({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(getProductTypes, payload);
            yield put({
                type: "app/updateState",
                payload: {
                    loading: true
                }
            });
            yield put({
                type: "updateState",
                payload: {
                    productTypes: res.object.content,
                    totalElements: res.object.totalElements
                }
            });
        },
        * deleteProductType({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(deleteProductType, payload);
            if (res.success) {
                toast.success('Saqlandi')
                yield put({
                    type: "getProductTypes",
                    payload: {
                        page: 0,
                        size: 10
                    }
                });
            }
        },
        * getSearchedProductTypes({payload}: any, {call, put, select}: any) {
            const res = yield call(getSearchedProductTypes, payload);
            if (res.success) {
                yield put({
                    type: "updateState",
                    payload: {
                        searchedProductTypes: res.object.content.length ? res.object.content : '404'
                    }
                })
                // toast.success(data.message)
            } else {
                toast.error(res.message);
            }
            return res
        },
    }
})
