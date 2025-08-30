module.exports = {
    // Other Jest configurations...
    moduleNameMapper: {
        "^@/(.*)$": "<rootDir>/src/$1"
    },
    transform: {
        "^.+\\.ts$": "ts-jest"
    }
};