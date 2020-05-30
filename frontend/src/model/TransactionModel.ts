import { LocalDate } from "@js-joda/core";

export default class TransactionModel {

    private _subject: string;
    private _date: LocalDate;
    private _amount: number;


    constructor($subject: string, $date: LocalDate, $amount: number) {
        this._subject = $subject;
        this._date = $date;
        this._amount = $amount;
    }

    /**
     * Getter $subject
     * @return {string}
     */
    public get subject(): string {
        return this._subject;
    }

    /**
     * Getter $date
     * @return {LocalDate}
     */
    public get date(): LocalDate {
        return this._date;
    }

    /**
     * Getter $amount
     * @return {number}
     */
    public get amount(): number {
        return this._amount;
    }

    /**
     * Setter $subject
     * @param {string} value
     */
    public set subject(value: string) {
        this._subject = value;
    }

    /**
     * Setter $date
     * @param {LocalDate} value
     */
    public set date(value: LocalDate) {
        this._date = value;
    }

    /**
     * Setter $amount
     * @param {number} value
     */
    public set amount(value: number) {
        this._amount = value;
    }






}