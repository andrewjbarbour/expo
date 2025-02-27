import { PermissionResponse, PermissionStatus, PermissionHookOptions } from 'expo-modules-core';
import * as React from 'react';
import { ViewProps } from 'react-native';
export declare type BarCodePoint = {
    x: number;
    y: number;
};
export declare type BarCodeSize = {
    height: number;
    width: number;
};
export declare type BarCodeBounds = {
    origin: BarCodePoint;
    size: BarCodeSize;
};
export declare type BarCodeScannerResult = {
    type: string;
    data: string;
    bounds?: BarCodeBounds;
    cornerPoints?: BarCodePoint[];
};
export declare type BarCodeEvent = BarCodeScannerResult & {
    target?: number;
};
export declare type BarCodeEventCallbackArguments = {
    nativeEvent: BarCodeEvent;
};
export { PermissionResponse, PermissionStatus, PermissionHookOptions };
export declare type BarCodeScannedCallback = (params: BarCodeEvent) => void;
export interface BarCodeScannerProps extends ViewProps {
    type?: 'front' | 'back' | number;
    barCodeTypes?: string[];
    onBarCodeScanned?: BarCodeScannedCallback;
}
export declare class BarCodeScanner extends React.Component<BarCodeScannerProps> {
    lastEvents: {
        [key: string]: any;
    };
    lastEventsTimes: {
        [key: string]: any;
    };
    static Constants: {
        BarCodeType: any;
        Type: any;
    };
    static ConversionTables: {
        type: any;
    };
    static defaultProps: {
        type: any;
        barCodeTypes: unknown[];
    };
    static getPermissionsAsync(): Promise<PermissionResponse>;
    static requestPermissionsAsync(): Promise<PermissionResponse>;
    /**
     * Check or request permissions for the barcode scanner.
     * This uses both `requestPermissionAsync` and `getPermissionsAsync` to interact with the permissions.
     *
     * @example
     * ```ts
     * const [status, requestPermission] = BarCodeScanner.usePermissions();
     * ```
     */
    static usePermissions: (options?: PermissionHookOptions<object> | undefined) => [PermissionResponse | null, () => Promise<PermissionResponse>, () => Promise<PermissionResponse>];
    static scanFromURLAsync(url: string, barCodeTypes?: string[]): Promise<BarCodeScannerResult[]>;
    render(): JSX.Element;
    onObjectDetected: (callback?: BarCodeScannedCallback | undefined) => ({ nativeEvent }: BarCodeEventCallbackArguments) => void;
    convertNativeProps(props: BarCodeScannerProps): BarCodeScannerProps;
}
export declare const Constants: {
    BarCodeType: any;
    Type: any;
}, getPermissionsAsync: typeof BarCodeScanner.getPermissionsAsync, requestPermissionsAsync: typeof BarCodeScanner.requestPermissionsAsync, usePermissions: (options?: PermissionHookOptions<object> | undefined) => [PermissionResponse | null, () => Promise<PermissionResponse>, () => Promise<PermissionResponse>];
