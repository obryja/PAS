import * as Yup from 'yup';

export const rentAddSchema = Yup.object({
    selectedUser: Yup.string().required('Użytkownik jest wymagany'),
    selectedBook: Yup.string().required('Książka jest wymagana'),
    beginDate: Yup.string().required('Data rozpoczęcia jest wymagana')
    .matches(
        /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$/, 
        'Data musi być w formacie YYYY-MM-DD HH:mm'
    ),
});