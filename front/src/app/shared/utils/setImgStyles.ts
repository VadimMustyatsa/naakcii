export default function setImgStyles(pict: string): {} {
    return {
      'background-image': `url("${pict}")`,
      'background-size': 'contain',
      'background-repeat': 'no-repeat',
      'background-position': 'center',
      'margin': '10px'
    };
  }
