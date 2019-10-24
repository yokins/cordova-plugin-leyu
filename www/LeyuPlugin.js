var exec = require('cordova/exec');

const leyu = {
  /**
   * @description: 开启服务
   * @param {type}
   * @return:
   */
  openService: function() {
    exec(() => {}, () => {}, 'LeyuPlugin', 'attemptToBindService', []);
  },
  /**
   * @description: 关闭服务
   * @param {type}
   * @return:
   */
  closeService: function() {
    exec(() => {}, () => {}, 'LeyuPlugin', 'unbindService', []);
  },
  /**
   * @description: 获取状态
   * @param {type}
   * @return:
   */
  getTpEnable: function() {
    return new Promise(resolve => {
      exec(
        success => {
          if (success) {
            resolve(true);
          }
        },
        error => {
          if (error) {
            resolve(false);
          }
        },
        'LeyuPlugin',
        'getTpEnable',
        []
      );
    });
  },
  /**
   * @description: 设置状态
   * @param {type}
   * @return:
   */
  setTpEnable: function(enable) {
    exec(() => {}, () => {}, 'LeyuPlugin', 'setTpEnable', [enable]);
  }
  // coolMethod: function(arg0, success, error) {
  //   exec(success, error, 'LeYu', 'coolMethod', [arg0]);
  //   console.log('coolMethod, in');
  // },
  // setTpEnable: function(enable) {
  //   console.info('---LeyuPlugin---setTpEnable---');
  //   return new Promise(resolve => {
  //     exec(
  //       () => {
  //         resolve({ status: true });
  //       },
  //       () => {
  //         resolve({ status: false });
  //       },
  //       'LeyuPlugin',
  //       'setTpEnable',
  //       [{ enable: enable }]
  //     );
  //   });
  // },
  // getTpEnable: function() {
  //   console.info('---LeyuPlugin---getTpEnable---');
  //   return new Promise(resolve => {
  //     exec(
  //       enable => {
  //         resolve({ status: true, enable: enable });
  //       },
  //       () => {
  //         resolve({ status: false });
  //       },
  //       'LeyuPlugin',
  //       'getTpEnable',
  //       []
  //     );
  //   });
  // }
};

module.exports = leyu;
