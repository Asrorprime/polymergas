import React from 'react';
import './CurrentCalculateComponentStyles.less'
import {Button} from "reactstrap";
// @ts-ignore
import {AvField, AvForm} from "availity-reactstrap-validation";
import {connect} from "dva";

interface initialState {

}

interface initialState {
  sum: number,
}

// @ts-ignore
@connect(({maincolor}) => ({maincolor}))
class CurrentCalculateLinksOne extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {
      sum: 0,
    }
  }

  render() {
    const {maincolor, dispatch, app,level, coating,setAnswer} = this.props;
    const {currentMainColor} = maincolor;
    const setMyState = (e: any) => {

      this.setState({[e.target.name]: e.target.value})
    }
    const colculate = () => {

      setAnswer(this.state.sum / level / coating)
    }
    return (
      <div className="currentCalculateLinksOneComponent">
        <div className="text-center my-3">
          <p className="mb-0 fs-14 font-family-semi-bold">
            Ajoyib! Uni quyida qo'shing va sizga qancha bo'yoq kerakligini bilib oling.
          </p>
          <AvForm className=" w-100">
            <div className="d-flex justify-content-center flex-column w-100">
              <div className="">
                <div className="d-flex flex-column justify-content-center">
                  <div className="">
                    <AvField className="modalSectionsInput" name="sum" label="Mintaqa kvadrat metrda"
                             placeholder="Mintaqa"
                             onChange={setMyState}
                      // value={currentMainColor.height}
                             validate={
                               {
                                 required: {value: true, errorMessage: "Mintaqa kvadrat ni kiriting!"}
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
        </div>
      </div>
    );
  }
}

export default CurrentCalculateLinksOne;
