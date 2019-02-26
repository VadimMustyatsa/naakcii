export default function roundTo2Digits(n: number, digPos: number = 2): number {
    const k = Math.pow(10, digPos);
    return Math.round(n * k) / k;
  }
