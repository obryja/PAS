import * as Yup from 'yup';

export const userUpdateSchema = Yup.object().shape({
    username: Yup.string()
        .min(3, 'Nazwa użytkownika musi mieć co najmniej 3 znaki.')
        .max(50, 'Nazwa użytkownika nie może przekroczyć 50 znaków.')
        .required('Nazwa użytkownika jest wymagana.'),
});