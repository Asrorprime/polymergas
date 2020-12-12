import React, {Component} from 'react';
import {Button, Modal, ModalBody, ModalHeader} from 'reactstrap';
import {connect} from "@@/plugin-dva/exports";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';

// @ts-ignore


interface initialState {
    file: any,
    priceList: { price: null, reqQuantity: { quantityType: null, value: null }, haveProduct: null }[],
}

// @ts-ignore
@connect(({product, app, category, productType}) => ({product, app, category, productType}))
class AddProduct extends Component {
    state: initialState;
    props: any;

    constructor(props: any) {
        super(props);
        this.state = {
            maxDate: null,
            minDate: null,
            file: [],
            priceList: [{price: null, reqQuantity: {quantityType: null, value: null}, haveProduct: null}]
        }
        // this.onChange = this.onChange.bind(this);
    }

    componentWillUnmount(): void {
    }

    componentDidMount(): void {
        const {dispatch} = this.props;
        dispatch({
            type: 'category/getCategories'
        });
        dispatch({
            type: 'productType/getProductTypes'
        });
        const edit = this.props.value.current;

        if (edit.id) {
            this.state.priceList = [];
            edit.photos.forEach((file: { fileId: any; }) => this.state.file.push(file.fileId));
            // @ts-ignore
            edit.resProductPrices.forEach(info => this.state.priceList.push({
                id: info.id,
                reqQuantity: {
                    id: info.resQuantity.id,
                    quantityType: info.resQuantity.quantityType,
                    value: info.resQuantity.value
                },
                price: info.price,
                productPriceId: info.productPriceId,
                haveProduct: info.haveProduct,
            }));
            this.setState(this.state)
        } else {
        }
    }


    // @ts-ignore
    onChange(event: { target: { files: any; name: any; }; }) {
        this.props.dispatch({
            type: "app/uploadFile",
            payload: {
                file: event.target.files,
                fileUpload: true,
                type: event.target.name
            }
        }).then((res: any) => {
            this.setState({
                file: [...res]
            });
        })

    }

    render() {
        const props = this.props;
        // @ts-ignore
        const {
            className, dispatch,
        } = props;
        const {category, productType} = props;
        const {categories} = category;
        const {productTypes} = productType;
        const {priceList} = this.state;

        const edit = props.value.current;
        const savePhoto = (e: any, img: any) => {
            dispatch({
                type: "app/uploadFile",
                payload: {
                    file: event.target.files,
                    fileUpload: true,
                    type: event.target.name
                }
            }).then((res: any) => {
                if (e === 'edit') {
                    this.state.file.map((photo, index) => {
                            if (photo === img) {
                                this.state.file[index] = res;
                            }
                        }
                    )
                } else {
                    this.state.file.push(res);
                }
                this.setState(this.state);
            })
        };
        const delPhoto = (v: any) => {
            this.state.file = this.state.file.filter(photos => photos !== v);
            this.setState(this.state);
        };
        const addColum = (isMain: any) => {
            this.state.priceList.push({
                price: null,
                reqQuantity: {quantityType: null, value: null},
                haveProduct: null
            })
            this.setState(this.state);
        };
        const delColum = (isMain: any, index: any) => {
            if (isMain === 'count') {
                // @ts-ignore
                if (this.state.priceList.length > 1) {
                    this.state.priceList = this.state.priceList.filter((itm, indx) => indx !== index)
                }
            }
            this.setState(this.state);
        };
        const saveProduct = (a: any, v: any) => {
            Object.keys(v).map(key => {
                if (key.includes("priceO")) {
                    if (key.includes("value")) {

                    } else if (key.includes("priceProduct")) {
                        // @ts-ignore
                        priceList[key.split("_")[1]]['price'] = v[key]
                    } else {
                        // @ts-ignore
                        priceList[key.split("_")[1]]['haveProduct'] = v[key]
                    }
                }
                // @ts-ignore
                priceList[0].reqQuantity.value = '1';
                priceList[0].reqQuantity.quantityType = "kg";
            });
            this.props.dispatch({
                type: "app/saveProduct",
                payload: {
                    id: edit.id ? edit.id : null,
                    madeIn: v.madeIn,
                    expirationDate: v.expirationDate,
                    productionDate: v.productionDate,
                    productTypeId: v.productTypeId,
                    categoryId: v.categoryId,
                    provider: v.provider,
                    name: v.name,
                    descriptionUz: v.descriptionUz,
                    descriptionRu: v.descriptionRu,
                    descriptionEn: v.descriptionEn,
                    reqProductPrices: priceList,
                    photosId: this.state.file
                }
            }).then((res: any) =>
                dispatch({
                    type: 'app/getProduct',
                    payload: {
                        page: 0,
                        size: 10
                    }
                }));
            this.props.value.toggle(null)
        };

        const setValToState = (a, v, status, index) => {
            if (status === 'measurecost') {
                this.state.priceList[index].reqQuantity.value = '1'
            } else if (status === 'valuecost') {
                this.state.priceList[index].price = v
            }
            this.setState(this.state)
        };
        const setValHaveProduct = (a, v, index) => {
            this.state.priceList[index].haveProduct = v;
            this.setState(this.state)
        };

        // @ts-ignore
        return (
            <div className='right-modal'>
                <Modal id="leftsite" size='xl' centered={true} isOpen={props.value.isOpen} className={className}>
                    <AvForm onValidSubmit={saveProduct}>
                        <ModalHeader className='font-s-20 position-relative mt-2 pl-4'
                                     toggle={() => this.props.value.toggle(null)}>Mahsulot
                            qo’shish</ModalHeader>
                        <ModalBody style={{minHeight: '75vh'}}>
                            <div className="container-fluid mt-3 p-0">
                                <div className="row mb-3">
                                    {this.state.file ? this.state.file.map((img: string) =>
                                            <div className="col-md-2 pr-0 position-relative cent">
                                                <div className="position-relative hov-head  d-flex"
                                                     style={{alignItems: 'center'}}>
                                                    <div className='hover-bg text-center position-absolute m-auto w-100'
                                                         style={{zIndex: 2}}>
                          <span className='btn btn-light p-1'>                        <input className='editInput'
                                                                                             id="uploadFile"
                                                                                             name="mainAttachmentId"
                                                                                             type="file"
                                                                                             onChange={savePhoto.bind(this, 'edit', img)}/>
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"><path fill="none" d="M0 0h24v24H0z"/><path
    d="M7.243 18H3v-4.243L14.435 2.322a1 1 0 0 1 1.414 0l2.829 2.829a1 1 0 0 1 0 1.414L7.243 18zM3 20h18v2H3v-2z"
    fill="rgba(147,144,144,1)"/></svg></span>
                                                        <span onClick={() => delPhoto(img)}
                                                              className='btn btn-light p-1 ml-2'><svg
                                                            xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
                                                            width="24" height="24"><path
                                                            fill="none" d="M0 0h24v24H0z"/><path
                                                            d="M17 6h5v2h-2v13a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V8H2V6h5V3a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1v3zm1 2H6v12h12V8zm-9 3h2v6H9v-6zm4 0h2v6h-2v-6zM9 4v2h6V4H9z"
                                                            fill="rgba(147,144,144,1)"/></svg></span>
                                                    </div>
                                                    <div className="bg-tr position-absolute"></div>
                                                    <img
                                                        src={"/api/file/" + img}
                                                        alt="" className='addImg'/>
                                                </div>
                                            </div>
                                    ) : null}
                                    <div className="col d-flex">
                                        <div className="uploadInput my-auto">
                                            <label className="upload-area">
                                                <input id="uploadFile" name="mainAttachmentId" type="file"
                                                       onChange={savePhoto}/>
                                                <span className="upload-button addImgButton">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="40" height="35"><path
                            fill="none" d="M0 0h24v24H0z"/><path
                            d="M21 15v3h3v2h-3v3h-2v-3h-3v-2h3v-3h2zm.008-12c.548 0 .992.445.992.993V13h-2V5H4v13.999L14 9l3 3v2.829l-3-3L6.827 19H14v2H2.992A.993.993 0 0 1 2 20.007V3.993A1 1 0 0 1 2.992 3h18.016zM8 7a2 2 0 1 1 0 4 2 2 0 0 1 0-4z"
                            fill="rgba(147,144,144,1)"/>
                        </svg>
                     </span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="row mt-3">
                                    <div className="col">
                                        <AvField label={'Nomini kiriting'} defaultValue={edit.id ? edit.name : null}
                                                 name="name"
                                                 placeHolder={'Marsheloo'} required/>
                                    </div>
                                    <div className="col">
                                        <AvField label={'Ishlab chiqaruvchi'}
                                                 defaultValue={edit.id ? edit.madeIn : null} name="madeIn"
                                                 type='text' placeHolder={"O'zbekiston"} required/>

                                    </div>
                                    {/*<div className="col">*/}
                                    {/*  <AvField label={'Yetqazib beruvchi'} defaultValue={edit.id ? edit.provider : null} name="provider"*/}
                                    {/*           type='textarea' placeHolder={'Yetqazib beruvchi ...'} required/>*/}
                                    {/*</div>*/}

                                </div>
                                <div className="row mt-3">
                                    <div className="col">
                                        <AvField label={'Qisqacha tarif (uz)'}
                                                 defaultValue={edit.id ? edit.descriptionUz : null}
                                                 name="descriptionUz"
                                                 type='textarea' placeHolder={'Qisqacha tarif (uz)...'} required/>
                                    </div>
                                    <div className="col">
                                        <AvField label={'Qisqacha tarif (ru)'}
                                                 defaultValue={edit.id ? edit.descriptionRu : null}
                                                 name="descriptionRu"
                                                 type='textarea' placeHolder={'Qisqacha tarif (ru)...'} required/>
                                    </div>
                                    <div className="col">
                                        <AvField label={'Qisqacha tarif (en)'}
                                                 defaultValue={edit.id ? edit.descriptionEn : null}
                                                 name="descriptionEn"
                                                 type='textarea' placeHolder={'Qisqacha tarif (en)...'} required/>
                                    </div>
                                    {/*<div className="col">*/}
                                    {/*  <AvField max={edit.id ? edit.expirationDate.substring(0, 10) : this.state.maxDate}*/}
                                    {/*           onChange={(a, v) => this.setState({minDate: v})}*/}
                                    {/*           defaultValue={edit.id ? edit.expirationDate.substring(0, 10) : null}*/}
                                    {/*           name="expirationDate"*/}
                                    {/*           label='Ishlab chiqarilgan sana' required type='date'/>*/}
                                    {/*</div>*/}

                                    {/*<div className="col">*/}
                                    {/*  <AvField defaultValue={edit.id ? edit.productionDate.substring(0, 10) : null} name="productionDate"*/}
                                    {/*           min={edit.id ? edit.productionDate.substring(0, 10) : this.state.minDate}*/}
                                    {/*           onChange={(a, v) => this.setState({maxDate: v})}*/}
                                    {/*           label='Amal qilish mudat' required type='date'/>*/}
                                    {/*</div>*/}
                                </div>
                                <div className="row mt-3">
                                    <div className="col">
                                        <AvField label="Mahsulot kategoriyasi"
                                                 defaultValue={edit.id ? edit.resCategory.categoryId : null}
                                                 name="categoryId" required
                                                 type='select'>
                                            <option>Kategoriyani tanlang</option>
                                            {edit.id ?
                                                <option
                                                    value={edit.resCategory.categoryId}>{edit.resCategory.nameUz}</option>
                                                // @ts-ignore
                                                : categories ? categories.map(item => {
                                                    return (<option value={item.categoryId}>{item.nameUz}</option>)
                                                }) : null}
                                        </AvField>
                                    </div>
                                    <div className="col">
                                        <AvField label="Mahsulot tipi"
                                                 defaultValue={edit.id ? edit.resProductType.productTypeId : null}
                                                 name="productTypeId" required
                                                 type='select'>
                                            <option>Mahsulot tipini tanlang</option>
                                            {edit.id ?
                                                <option
                                                    value={edit.resProductType.productTypeId}>{edit.resProductType.nameUz}</option>
                                                // @ts-ignore
                                                : productTypes ? productTypes.map(item => {
                                                    return (<option value={item.productTypeId}>{item.nameUz}</option>)
                                                }) : null}
                                        </AvField>
                                    </div>
                                </div>
                                <div className="row my-4">
                                    {/*<div className="col-2 mb-3 mr-3">*/}
                                    {/*  <AvField label="O'lchov birligi"*/}
                                    {/*           defaultValue={edit.id ? this.state.priceList[0].reqQuantity.quantityType : null}*/}
                                    {/*           name={`quantityType`}*/}
                                    {/*           type='select'*/}
                                    {/*           required>*/}
                                    {/*    <option>O'lchovni tanlang</option>*/}
                                    {/*    <option value={'kg'}>Kilogram</option>*/}
                                    {/*    <option value={'countable'}>Sanoq</option>*/}
                                    {/*  </AvField>*/}
                                    {/*</div>*/}
                                    {this.state.priceList.map((item, index) =>
                                        <div className='col-12 mb-4 complekt position-relative'>
                                            {/*<div onClick={() => delColum('count', index)} className=" ml-auto p-1 x-button">*/}
                                            {/*  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">*/}
                                            {/*    <path fill="none" d="M0 0h24v24H0z"/>*/}
                                            {/*    <path*/}
                                            {/*      d="M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16zm0-9.414l2.828-2.829 1.415 1.415L13.414 12l2.829 2.828-1.415 1.415L12 13.414l-2.828 2.829-1.415-1.415L10.586 12 7.757 9.172l1.415-1.415L12 10.586z"*/}
                                            {/*      fill="rgba(115,115,115,1)"/>*/}
                                            {/*  </svg>*/}
                                            {/*</div>*/}
                                            <div className="row mb-3">
                                                <div className="col-4">
                                                    <AvField
                                                        onChange={(a, v) => setValToState(a, v, 'valuecost', index)}
                                                        label="1 kg narxi"
                                                        value={item.price}
                                                        placeHolder={'1 kg narxi'}
                                                        name={`priceProduct_${index}_priceO`}
                                                        required
                                                        min={0}
                                                        type='number'/>
                                                </div>
                                                <div className="col-4">
                                                    <div className="product-have mx-3">
                                                        <AvField
                                                            label="Mahsulot sotuvda bormi?"
                                                            name={`haveProduct_${index}_priceO`}
                                                            onChange={(a, v) => setValHaveProduct(a, v, index)}
                                                            value={item.haveProduct} type="checkbox"
                                                        />
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    )}
                                    {/*<div className="col-1 mb-3 mt-5">*/}
                                    {/*  <div onClick={() => addColum('count')} className="btn btn-light">*/}
                                    {/*    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="18" height="18">*/}
                                    {/*      <path fill="none" d="M0 0h24v24H0z"/>*/}
                                    {/*      <path*/}
                                    {/*        d="M11 11V7h2v4h4v2h-4v4h-2v-4H7v-2h4zm1 11C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16z"*/}
                                    {/*        fill="rgba(101,101,101,1)"/>*/}
                                    {/*    </svg>*/}
                                    {/*  </div>*/}
                                    {/*</div>*/}
                                </div>
                            </div>

                            <div className="mt-4">
                                <div className="d-flex justify-content-start">
                                    <div className="">
                                        <Button className="benefitBtn addBtnBenefit mr-3" type='submit'>Saqlash</Button>
                                        <Button className="benefitBtn cancelBtnBenefit"
                                                onClick={() => this.props.value.toggle(null)}>Bekor
                                            qilish</Button>
                                    </div>
                                </div>
                            </div>
                        </ModalBody>
                    </AvForm>
                </Modal>
            </div>
        );
    }
}

export default AddProduct;
