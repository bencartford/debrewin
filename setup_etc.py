confirmed_deb_seq = []
cancelled_deb_seq = []
max_length_n = 32
totalNLengthSequences = []
deb_seq_array = []


class DebSequence:

    def __init__(self, k, n):
        self.k = k
        self.n = n
        print('THIS IS ALL DEB SEQ: {}'.format(self.get_all_deb_seq(k, n)))

    @staticmethod
    def is_deb_seq(seq, window):
        preexisting_sequences = []
        elongated_sec = seq + seq[0:window-1]
        size = len(elongated_sec)
        for i in range(size - window + 1):
            sub_string = elongated_sec[i:i + window]
            if sub_string in preexisting_sequences:
                return False
            else:
                preexisting_sequences.append(sub_string)
        return True

    @staticmethod
    def shuffle_deb_seq(given_seq):
        shuffled_sequence = [None] * max_length_n
        for i in range(max_length_n):
            if i < 16:
                shuffled_sequence[i*2] = given_seq[i]
            else:
                multiplier = abs(max_length_n - (2 * i)) + 1
                shuffled_sequence[multiplier] = given_seq[i]
        shuffled_sequence = ''.join(shuffled_sequence)  # opportunity for exception checker
        print(shuffled_sequence)
        if shuffled_sequence == given_seq:
            print("they're the same!")


    # we're going to be shifting the sequence to the right
    # ex: original: 123456
    #     new:      612345
    @staticmethod
    def shift_the_sequence(seq):
        shifted_seq_array = [None]*len(seq)
        for i in range(len(seq)):
            if i == len(seq)-1:
                shifted_seq_array[0] = seq[i]
            else:
                shifted_seq_array[i+1] = seq[i]
        return shifted_seq_array


    # Function to print the output
    @staticmethod
    def append_list_of_total_seq(arr, n):
        final_string = ""
        for iteration in range(0, n):
            final_string += str(arr[iteration])
        totalNLengthSequences.append(final_string)


    # Function to generate all binary strings
    @staticmethod
    def make_unique_window_sequences(max_size, arr, current_size, alphabet_array):
        if current_size == max_size:
            DebSequence.append_list_of_total_seq(arr, max_size)
            return totalNLengthSequences
        for number in alphabet_array:
            arr[current_size] = number
            DebSequence.make_unique_window_sequences(max_size, arr, current_size + 1, alphabet_array)

    @staticmethod
    def get_unique_window_sequences(m, a, c, aa):
        DebSequence.make_unique_window_sequences(m, a, c, aa)
        return totalNLengthSequences

    @staticmethod
    def build_on_this_base(growing_sequence, max_length, n_length_sequence_list, window_size, already_used_sequences):
        if len(growing_sequence) == max_length:
            if DebSequence.is_deb_seq(growing_sequence, window_size):
                deb_seq_array.append(growing_sequence)
        else:
            for n in n_length_sequence_list: # just subtract (opposite of append) to leave "remaining possibilities"
                if n not in already_used_sequences:
                    start_point = len(growing_sequence)-window_size+1
                    if n[:window_size-1] == growing_sequence[start_point:len(growing_sequence)]:
                        already_used_sequences.append(n)
                        growing_sequence += n[window_size-1]
                        DebSequence.build_on_this_base(growing_sequence, max_length, n_length_sequence_list, window_size,
                                           already_used_sequences)

    @staticmethod
    def make_alphabet_array(alpha_possibilities):
        alpha_options_array = []
        for i in range(alpha_possibilities):
            alpha_options_array.append(i)
        return alpha_options_array


    @staticmethod
    def get_all_deb_seq(alpha, window_size):
        max_seq_length = alpha ** window_size
        input_array = [None]*window_size
        alpha_op_array = DebSequence.make_alphabet_array(alpha)
        n_length_seq = DebSequence.get_unique_window_sequences(window_size, input_array, 0, alpha_op_array)
        for i in n_length_seq:
            prev_used_sequences = [i]
            DebSequence.build_on_this_base(i, max_seq_length, n_length_seq, window_size, prev_used_sequences)
        return deb_seq_array

    @staticmethod
    def just_make_sure(given_output):
        for i in given_output:
            if DebSequence.is_deb_seq(i, 5):
                confirmed_deb_seq.append(i)
            else:
                cancelled_deb_seq.append(i)


print(DebSequence(2, 2))
