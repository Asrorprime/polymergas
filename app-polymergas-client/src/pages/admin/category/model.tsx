// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import {model} from '@/utils/model'
// @ts-ignore
import api from 'api'
// @ts-ignore
import {toast} from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';

const {deleteCategory, saveCategory, getCategories, getSearchedCategories} = api;

export default modelExtend(model, {
    namespace: 'category',
    state: {
        totalElements: 0,
        categories: [],
        searchedCategories: [],
    },
    subscriptions: {
        setup({dispatch, history}: { dispatch: any, history: any }) {
            history.listen((location: any) => {

            })
        }
    },
    effects: {
        * saveCategory({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(saveCategory, payload);
            yield put({
                type: "app/updateState",
                payload: {
                    loading: true
                }
            });
            if (res.success) {
                toast.success('Saqlandi')
                yield put({
                    type: "getCategories",
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
        * getCategories({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(getCategories, payload);
            yield put({
                type: "app/updateState",
                payload: {
                    loading: true
                }
            });
            yield put({
                type: "updateState",
                payload: {
                    categories: res.object.content,
                    totalElements: res.object.totalElements
                }
            });
        },
        * deleteCategory({payload}: { payload: any }, {call, put, select}: any) {
            const res = yield call(deleteCategory, payload);
            if (res.success) {
                toast.success('Saqlandi')
                yield put({
                    type: "getCategories",
                    payload: {
                        page: 0,
                        size: 10
                    }
                });
            }
        },
        * getSearchedCategories({payload}: any, {call, put, select}: any) {
            const res = yield call(getSearchedCategories, payload);
            if (res.success) {
                yield put({
                    type: "updateState",
                    payload: {
                        searchedCategories: res.object.content.length ? res.object.content : '404'
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
