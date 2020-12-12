import React, {Component} from 'react';
interface  initialState{
}
class Footer extends Component {
  state:initialState;
  constructor(props:any) {
    super(props);
    this.state={
      type:""
    }
  }
  render() {
    return (
      <div>
        Footer
      </div>
    );
  }
}

export default Footer;
