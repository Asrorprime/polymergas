import React from 'react';
import './CurrentCalculateComponentStyles.less'
import {Button} from "reactstrap";
// @ts-ignore
import {AvField, AvForm} from "availity-reactstrap-validation";
import {connect} from "dva";

interface initialState {
  height: number,
  width: number,
  colculated: number
}

// @ts-ignore
@connect(({maincolor}) => ({maincolor}))
class CurrentCalculateLinksZero extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {
      height: 0,
      width: 0,
      colculated: 0
    }
  }

  render() {
    const {maincolor, dispatch, app, level, coating, setAnswer} = this.props;
    const {currentMainColor} = maincolor;
    const colculate = () => {
      setAnswer(this.state.height * this.state.width / level / coating)
    }
    const setMyState = (e: any) => {

      this.setState({[e.target.name]: e.target.value})
    }
    return (
      <div className="currentCalculateLinksZeroComponent">
        <div className="text-center my-3">
          <p className="mb-0 fs-14 font-family-semi-bold">
            Bo'yashni xohlagan sirtning balandligi va uzunligini kiriting.
            <AvForm className=" w-100">
              <div className="d-flex justify-content-center flex-column w-100">
                <div className="">
                  <div className="d-flex flex-column justify-content-center">
                    <div className="">
                      <AvField className="modalSectionsInput" name="height" label="Balandligi metrda"
                               placeholder="Balandlik"
                               onChange={setMyState}
                        // value={currentMainColor.height}
                               validate={
                                 {
                                   required: {value: true, errorMessage: "Balandlik ni kiriting!"}
                                 }
                               }/>
                    </div>
                    <div className="">
                      <AvField className="modalSectionsInput" name="width" label="Uzunlik metrda" placeholder="Uzunlik"
                               onChange={setMyState}
                        // value={currentMainColor.weight}
                               validate={
                                 {
                                   required: {value: true, errorMessage: "Uzunlik ni kiriting!"}
                                 }
                               }/>
                    </div>
                  </div>
                </div>
                <div className="my-3">
                  <div className="text-center">
                    <Button className="benefitBtn addBtnBenefit" onClick={colculate}>Hisoblash</Button>
                  </div>
                </div>
              </div>
            </AvForm>
          </p>
        </div>
      </div>
    );
  }
}

export default CurrentCalculateLinksZero;
