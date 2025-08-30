import {onlyTwoPrecision} from "@/helper/amount-utils";

describe('amount-utils', () => {
    describe('onlyTwoPrecision', () => {
        it.each([
            [null, true],
            [undefined, true],
            [12, true],
            [12.12, true],
            [12.124, false],
            [12124, true],
            [123.6, true],
        ])
        ('should validate %s to %s', (value: number | null | undefined, expected: boolean) => {
            expect(onlyTwoPrecision(value)).toBe(expected);
        });
    });
});