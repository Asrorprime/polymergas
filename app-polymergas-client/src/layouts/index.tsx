import React from 'react';
import {connect} from "dva";
import {toast, ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface  initialState{
}
// @ts-ignore
@connect(({app}) => ({app}))
class BasicLayout extends React.Component {
  componentDidMount() {
    // @ts-ignore
    const {dispatch} = this.props;
    dispatch({
      type: 'app/userMe'
    })
  }
  state:initialState;
  constructor(props:any) {
    super(props);
    this.state={

    }
  }
  render() {
    return (
      <div>
        {/*<Header/>*/}
        <ToastContainer />
        {this.props.children}
        {/*<Footer/>*/}
        </div>
    );
  }
}

export default BasicLayout;
