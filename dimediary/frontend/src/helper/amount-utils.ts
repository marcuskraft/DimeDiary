

export const onlyTwoPrecision = (value: number | null | undefined): boolean => {
    if (!value) {
        return true;
    }

    const numString = String(value);
    const parts = numString.split('.');

    switch (parts.length) {
        case 1: {
            return true;
        }
        case 2: {
            return parts[1].length < 3;
        }
        default: {
            return false;
        }
    }

}
