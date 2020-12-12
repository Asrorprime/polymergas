import React from 'react';
import './index.less'
import '../../../global.less'
import {Card, CardBody, Col, Row} from "reactstrap";
// @ts-ignore
import 'react-toastify/dist/ReactToastify.css';
import BenefitAdminLinksComponent from "@/component/BenefitAdminLinks/BenefitAdminLinksComponent";
import ReactApexChart from "react-apexcharts";
// @ts-ignore
import {Sparklines, SparklinesLine} from 'react-sparklines';
import {connect} from "@@/plugin-dva/exports";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';
import {FaRegBell} from "react-icons/all";
import {Link} from "umi";

interface initialState {
    series: any[] | undefined;
    options: object | undefined;
    seriesRadialTwo: any[] | undefined;
    optionsRadialTwo: object | undefined;
    from: Date | undefined,
    to: Date | undefined,
}

// @ts-ignore
@connect(({dashboard, app}) => ({dashboard, app}))
class Index extends React.Component {
    state: initialState;
    props: any

    constructor(props: any) {
        super(props);
        this.state = {
            from: undefined,
            to: undefined,
            optionsRadialTwo: {
                seriesRadialTwo: [
                    {
                        data: [],
                    },
                ],
                chart: {
                    type: 'bar',
                    height: 430
                },
                plotOptions: {
                    bar: {
                        horizontal: true,
                        dataLabels: {
                            position: 'top',
                        },
                    }
                },
                dataLabels: {
                    enabled: true,
                    offsetX: -6,
                    style: {
                        fontSize: '12px',
                        colors: ['#fff']
                    }
                },
                stroke: {
                    show: true,
                    width: 1,
                    colors: ['#fff']
                },
                xaxis: {
                    categories: [],
                },
            },


            options: {
                series: [],
                chart: {
                    height: 350,
                    type: 'area'
                },
                dataLabels: {
                    enabled: false
                },
                colors: [
                    "#36beff",
                    "#a890d3",
                    "#00E396",
                    "#fd9f4d",
                ],
                stroke: {
                    curve: 'smooth'
                },
                xaxis: {
                    type: 'sting',
                    categories: []
                },
                tooltip: {
                    x: {
                        format: 'dd/MM/yy HH:mm'
                    },
                },
            },
        }
    }

    componentDidMount(): void {
        var d = new Date(),
            month = '' + (d.getMonth() + 1),
            day = '' + (d.getDate()),
            year = d.getFullYear();
        let fromyear = '' + (d.getFullYear() - 1);
        let fromMount = '' + (d.getMonth() + 2)
        if (fromMount.length < 2) fromMount = '0' + fromMount;
        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;
        const from = [fromyear, fromMount, day].join('-');
        const to = [year, month, day].join('-');
        this.setState({from, to})
        this.props.dispatch({
            type: 'dashboard/getDashboardInfo',
            payload: {
                from: `${from} 09:57:38.994000`,
                to: `${to} 09:57:38.994000`,
                type: "month"
            }
        }).then(res => {
            if (res.success) {
                let xaxis = [];
                let topOption = [{name: 'Sotilgan soni', data: []}];
                let topxaxix: any[] = [];
                let seriesRadialTwo = [];
                let result = [
                    {
                        name: 'Foydalanuvchilar soni',
                        data: []
                    },
                    {
                        name: 'Aborot',
                        data: []
                    },
                    {
                        name: 'Mahsulotlar',
                        data: []
                    },
                ];
                if (res.object.top10Product) {
                    res.object.top10Product.map((product: any) => {
                        // @ts-ignore
                        topOption[0].data.push(product[1])
                        topxaxix.push(product[3])
                    })
                }
                if (res.object.abrotStatistic) {
                    Object.keys(res.object.abrotStatistic).forEach((mounth: any) =>
                        xaxis.push(mounth)
                    );
                }
                if (res.object.usersStatistic) {
                    // @ts-ignore
                    Object.keys(res.object.usersStatistic).forEach((userKey) =>
                        result[0].data.push(res.object.usersStatistic[userKey])
                    );
                }
                if (res.object.productStatictic) {
                    // @ts-ignore
                    Object.keys(res.object.productStatistic).forEach((userKey) =>
                        result[2].data.push(res.object.productStatistic[userKey])
                    );
                }
                if (res.object.abrotStatistic) {
                    // @ts-ignore
                    Object.keys(res.object.abrotStatistic).forEach((userKey) =>
                        result[1].data.push(res.object.abrotStatistic[userKey])
                    );
                }
                this.setState({
                    options: {
                        ...this.state.options,
                        xaxis: {
                            ...this.state.options.xaxis,
                            categories: xaxis
                        },
                    }
                });
                // @ts-ignore
                // @ts-ignore
                this.setState({
                    optionsRadialTwo: {
                        ...this.state.optionsRadialTwo,
                        xaxis: {
                            ...this.state.optionsRadialTwo.xaxis,
                            categories: topxaxix
                        },
                    }
                });
                this.setState({
                    optionsRadialTwo: {
                        ...this.state.optionsRadialTwo,
                        seriesRadialTwo: topOption
                    }
                });
                this.setState({
                    options: {
                        ...this.state.options,
                        series: result
                    }
                });
            }
        })
    }

    render() {
        const {infoForDashbord} = this.props.dashboard;
        // @ts-ignore
        const {data} = this.state;

        const {app} = this.props;
        const {currentUser, unReadNotifications} = app;
        const getByDate = (date: Date, status: any) => {
            if (status === 'from') {
                this.state.from = date;
            } else {
                this.state.to = date;
            }
            this.setState(this.state)
            this.props.dispatch({
                type: 'dashboard/getDashboardInfo',
                payload: {
                    from: `${this.state.from} 09:57:38.994000`,
                    to: `${this.state.to} 09:57:38.994000`,
                    type: new Date(date).getMonth() === new Date(date).getMonth() && new Date(date).getFullYear() === new Date(date).getFullYear() ? "day" : "month"
                }
            }).then(res => {
                if (res.success) {
                    let xaxis = [];
                    let topOption = [{name: 'Sotilgan soni', data: []}];
                    let topxaxix = [];
                    let seriesRadialTwo = [];
                    let result = [
                        {
                            name: 'Foydalanuvchilar soni',
                            data: []
                        },
                        {
                            name: 'Aborot',
                            data: []
                        },
                        {
                            name: 'Mahsulotlar',
                            data: []
                        },
                    ];
                    if (res.object.top10Product) {
                        res.object.top10Product.map(product => {
                            topOption[0].data.push(product[1])
                            topxaxix.push(product[3])
                        })
                    }
                    if (res.object.abrotStatistic) {
                        Object.keys(res.object.abrotStatistic).forEach(mounth =>
                            xaxis.push(mounth)
                        );
                    }
                    if (res.object.usersStatistic) {
                        // @ts-ignore
                        Object.keys(res.object.usersStatistic).forEach((userKey, index) =>
                            result[0].data.push(res.object.usersStatistic[userKey])
                        );
                    }
                    if (res.object.productStatistic) {
                        Object.keys(res.object.productStatistic).forEach((userKey, index) =>
                            result[2].data.push(res.object.productStatistic[userKey])
                        );
                    }
                    if (res.object.abrotStatistic) {
                        Object.keys(res.object.abrotStatistic).forEach((userKey, index) =>
                            result[1].data.push(res.object.abrotStatistic[userKey])
                        );
                    }

                    this.setState({
                        options: {
                            ...this.state.options,
                            xaxis: {
                                ...this.state.options.xaxis,
                                categories: xaxis
                            },
                        }
                    });
                    this.setState({
                        optionsRadialTwo: {
                            ...this.state.optionsRadialTwo,
                            xaxis: {
                                ...this.state.optionsRadialTwo.xaxis,
                                categories: topxaxix
                            },
                        }
                    });
                    this.setState({
                        optionsRadialTwo: {
                            ...this.state.optionsRadialTwo,
                            seriesRadialTwo: topOption
                        }
                    });
                    this.setState({
                        options: {
                            ...this.state.options,
                            series: result
                        }
                    });
                }
            })
        };
        return (
            <div className="container-fluid dashboard position-relative p-0">
                <div className="position-absolute menuLinkIcon">
                    <BenefitAdminLinksComponent/>
                </div>
                <Row className="m-0">
                    <Col md={12} className="p-0">
                        <div className="app-navbar">
                            <div className="row m-0">
                                <div className="col-12 col-lg-2">
                                    <div className="d-flex align-items-center h-100">
                                        <p className="fs-12 pl-3 font-family-semi-bold mb-0">
                                            Dashboard
                                        </p>
                                    </div>
                                </div>
                                <div className="col-lg-10 d-none-mb h-88">
                                    <AvForm className="h-100">
                                        <div className="h-100">
                                            <div className="d-flex justify-content-between align-items-center h-100">
                                                <div className="">
                                                    <div className="d-flex align-items-center h-100 fromEnd">
                                                        <div className="">
                                                            <label className="fs-12 font-family-semi-bold">Dan</label>
                                                            <AvField max={this.state.to}
                                                                     onChange={(e, v) => getByDate(v, 'from')}
                                                                     value={this.state.from} name="startDate"
                                                                     placeholder="...dan" type="date"/>
                                                        </div>
                                                        <div className="ml-3">
                                                            <label className="fs-12 font-family-semi-bold">Gacha</label>
                                                            <AvField min={this.state.from}
                                                                     onChange={(e, v) => getByDate(v, 'to')}
                                                                     value={this.state.to} name="endDate"
                                                                     placeholder="...gacha" type="date"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div className="notifications">
                                                    <div className="d-flex align-items-center h-100">
                                                        <div className="mr-3">
                                                            <Link to="/admin/notification"
                                                                  className="text-decoration-none text-dark">
                                                                <div className="d-flex">
                                                                    <div className="">
                                                                        <FaRegBell
                                                                            style={{height: "18px", width: "16px"}}
                                                                            className={unReadNotifications.length === 0 ? "bellOut" : "bell"}/>
                                                                    </div>
                                                                    <div className="">
                                      <span className="">
                                        <span className={unReadNotifications.length === 0 ? '' : "pulse"}>
                                          <span
                                              className="d-flex align-items-center justify-content-center h-100 w-100">
                                            <span className="">
                                              {unReadNotifications.length === 0 ? '' : unReadNotifications.length}
                                            </span>
                                          </span>
                                        </span>
                                      </span>
                                                                    </div>
                                                                </div>
                                                            </Link>
                                                        </div>
                                                        <div className="ml-2">
                                                            <p className="fs-12 font-family-semi-bold mb-0">{currentUser ? currentUser.fullName : ""}</p>
                                                            <p className="fs-12 font-family-semi-bold mb-0">Administrator</p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </AvForm>
                                </div>
                            </div>
                        </div>
                        <Row className="pt-5 m-0">
                            <Col md={3} className="dashboardCard1">
                                <Card>
                                    <CardBody className="p-0">
                                        <div className="d-flex h-100 justify-content-start flex-column">
                                            <div className="px-4 py-3">
                                                <p className="fs-30 text-white font-family-bold mb-0">Foydalanuvchilar soni</p>
                                                <p
                                                    className="fs-20 text-white font-family-medium">{infoForDashbord ? infoForDashbord.usersCount : 0}</p>
                                            </div>
                                            <div className="mt-auto">
                                                <Sparklines data={[5, 10, 5, 20, 8, 15]} height={40}>
                                                    <SparklinesLine color="#36beff"/>
                                                </Sparklines>
                                            </div>
                                        </div>
                                    </CardBody>
                                </Card>
                            </Col>
                            <Col md={3} className="dashboardCard2">
                                <Card>
                                    <CardBody className="p-0">
                                        <div className="d-flex h-100 justify-content-start flex-column">
                                            <div className="px-4 py-3">
                                                <p className="fs-30 text-white font-family-bold mb-0">Aborot</p>
                                                <p
                                                    className="fs-20 text-white font-family-medium">{infoForDashbord ? infoForDashbord.aborotCount : 0}</p>
                                            </div>
                                            <div className="mt-auto">
                                                <Sparklines data={[5, 10, 5, 20, 8, 15]} height={40}>
                                                    <SparklinesLine color="#a890d3"/>
                                                </Sparklines>
                                            </div>
                                        </div>
                                    </CardBody>
                                </Card>
                            </Col>
                            <Col md={3} className="dashboardCard3">
                                <Card>
                                    <CardBody className="p-0">
                                        <div className="d-flex h-100 justify-content-start flex-column">
                                            <div className="px-4 py-3">
                                                <p className="fs-30 text-white font-family-bold mb-0">Zakaz aborot</p>
                                                <p
                                                    className="fs-20 text-white font-family-medium">{infoForDashbord ? infoForDashbord.aborotSum ? infoForDashbord.aborotSum : 0 : 0}</p>
                                            </div>
                                            <div className="mt-auto">
                                                <Sparklines data={[5, 10, 5, 20, 8, 15]} height={40}>
                                                    <SparklinesLine color="#00E396"/>
                                                </Sparklines>
                                            </div>
                                        </div>
                                    </CardBody>
                                </Card>
                            </Col>
                            <Col md={3} className="dashboardCard4">
                                <Card>
                                    <CardBody className="p-0">
                                        <div className="d-flex h-100 justify-content-start flex-column">
                                            <div className="px-4 py-3">
                                                <p className="fs-30 text-white font-family-bold mb-0">Mahsulotlar</p>
                                                <p
                                                    className="fs-20 text-white font-family-medium">{infoForDashbord ? infoForDashbord.productCount : 0}</p>
                                            </div>
                                            <div className="mt-auto">
                                                <Sparklines data={[5, 10, 5, 20, 8, 15]} height={40}>
                                                    <SparklinesLine color="#fd9f4d"/>
                                                </Sparklines>
                                            </div>
                                        </div>
                                    </CardBody>
                                </Card>
                            </Col>
                        </Row>
                    </Col>
                </Row>

                <Row className="m-0">
                    <Col md={6} className="">
                        <div className="card chartCard1">
                            <div className="card-body">
                                <p className="fs-17 font-family-semi-bold">Statistikalar</p>
                                <div id="chart">
                                    <ReactApexChart options={this.state.options} series={this.state.options.series}
                                                    type="area"
                                                    height={300}/>
                                </div>
                            </div>
                        </div>
                    </Col>
                    <Col md={6}>
                        <div className="card chartCard2">
                            <div className="card-body">
                                <p className="fs-17 font-family-semi-bold">Eng ko`p sotilgan mahsulotlar TOP-10</p>
                                <div id="chart">
                                    <ReactApexChart options={this.state.optionsRadialTwo}
                                                    series={this.state.optionsRadialTwo.seriesRadialTwo}
                                                    type="bar" height={300}/>
                                </div>
                            </div>
                        </div>
                    </Col>
                </Row>
            </div>
        );
    }
}

export default Index;
