
export enum Environment {
  Local = 'localdev'
}

declare global {
  interface Window {
    VISUAL_OVERVIEW_BASE_URL: string;
    VISUAL_OVERVIEW_ENV: string;
  }
}

export default function configureEnvironment() {
  const hostname = window && window.location && window.location.hostname;

  if (/^.*localhost.*/.test(hostname)) {
    window.VISUAL_OVERVIEW_ENV = Environment.Local;
    window.VISUAL_OVERVIEW_BASE_URL = '/api';
  } else {
    console.warn(`Cannot find environment for host name ${hostname}`);
  }
}
