var exec = require('cordova/exec');

const leyu = {
  // coolMethod: function(arg0, success, error) {
  //   exec(success, error, 'LeYu', 'coolMethod', [arg0]);
  //   console.log('coolMethod, in');
  // },

  // 设置
  setTpEnable: function(enable) {
    console.info('---leyu---setTpEnable---');
    return new Promise(resolve => {
      exec(
        (msg, enable) => {
          console.info(`${msg}：` + enable);
          resolve({ status: true });
        },
        error => {
          console.info(error);
          resolve({ status: false });
        },
        'LeYu',
        'setTpEnable',
        [{ enable: enable }]
      );
    });
  },

  // 获取状态
  getTpEnable: function() {
    console.info('---leyu---getTpEnable---');
    return new Promise(resolve => {
      exec(
        (msg, enable) => {
          console.info(`${msg}：` + enable);
          resolve({ status: true, enable: enable });
        },
        error => {
          console.info(error);
          resolve({ status: false });
        },
        'LeYu',
        'getTpEnable',
        []
      );
    });
  }
};

module.exports = leyu;

