import * as Yup from 'yup';

export const rentAddSchema = Yup.object({
    selectedUser: Yup.string().required('Użytkownik jest wymagany'),
    selectedBook: Yup.string().required('Książka jest wymagana'),
    beginDate: Yup.string().required('Data początkowa jest wymagana'),
});