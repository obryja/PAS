export default interface Rent {
    id: string;
    userId: string;
    bookId: string;
    beginDate: string;
    endDate: string | null;
}