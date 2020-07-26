# hi! thanks for checking this out. here's a quick runthrough of how the program works
# declare an instance of DebSequence like this: my_sequence = DebSequence(n,k)
#   you will automatically get back all of the debruijns sequences the alg has calculated
#       this alg is being worked on rn, so you won't get *all* of the possible ones, but all the strings you get will
#       indeed be debruijns sequences of the parameters you gave.
# if you want to check if something's a debruijns sequence, for now you do need to declare a random instance of
# DebruijnSequence and then say my_sequence.is_deb_seq('the sequence you're inquiring about', [k, aka window])
#   TODO: make this its own class
# if you want to shuffle the sequence, make an instance of DebruijnSequence, and then do
# my_sequence.shuffle_seq('the sequence you want to shuffle')
#   TODO: make this in the same class the checker is in (common denom: shouldn't need to declare instance of debseq)
#   it can shuffle odd len sequences too now!


# things being worked on right now (in decreasing priority):
#       - making unit tests (see test_setup_etc.py; some are already done)
#       - making the algorithm all encompassing (getting all possible sequences with a given n, k)
#       - making the algorithm exclude duplicates (defined as "deb sequences that are the same as each other, but are
#           shifted so they don't appear to be the same")
#       - making the calls for is_deb_seq and shuffle_seq their own class


import math
deb_seq_array = []
totalNLengthSequences = []


class DebSequence:

    def __init__(self, k, n):
        self.k = k
        self.n = n
        self.deb_seq = self.get_all_deb_seq(k, n)
        print('THIS IS ALL DEB SEQ: {}'.format(self.deb_seq))

    # this tests if a given sequence is a debruijns sequence. To use, do :
    # DebSequence.is_deb_seq('your sequence', [the window])
    # i.e. DebSequence.is_deb_seq('0110', 2) would return True.
    @staticmethod
    def is_deb_seq(seq, window):
        preexisting_sequences = []
        elongated_seq = seq + seq[0:window-1]
        size = len(elongated_seq)
        for i in range(size - window + 1):
            sub_string = elongated_seq[i:i + window]
            if sub_string in preexisting_sequences:
                return False
            else:
                preexisting_sequences.append(sub_string)
        return True

    # this shuffles the deb sequence using the *out shuffle* method. it uses the algorithm @reed made:
    # if (i <  k/2): i -> 2i
    # if (i >= k/2): i -> 2i - k + 1
    @staticmethod
    def shuffle_seq(given_seq):
        max_length = len(given_seq)
        shuffled_sequence = [None] * max_length
        for i in range(max_length):
            if i < math.ceil(float(max_length) / 2):
                shuffled_sequence[i*2] = given_seq[i]
                print('i ({}) < THREE -> {}'.format(i, (i*2)))
            else:
                if max_length % 2 == 0:
                    multiplier = abs(max_length - (2 * i)) + 1
                else:
                    multiplier = abs(max_length - (2 * i))
                shuffled_sequence[multiplier] = given_seq[i]
        if None in shuffled_sequence:
            return 'Shuffled sequence contains None:( {}'.format(shuffled_sequence)
        shuffled_sequence = ''.join(shuffled_sequence)
        if shuffled_sequence == given_seq:
            print("they're the same!")
        return shuffled_sequence

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
        shifted_seq_string = ''.join(shifted_seq_array)
        return shifted_seq_string

    # This is the method that's called once the individual window sequences are added onto each other and have reached
    # the max length of the sequence. It combines the array of individual chars into one string, and then adds that onto
    # the array that holds all of the sequences of that given max length
    @staticmethod
    def append_list_of_total_seq(arr, n):
        final_string = ""
        for iteration in range(0, n):
            final_string += str(arr[iteration])
        totalNLengthSequences.append(final_string)

    # Function to generate all possible string iterations of a the variable in a given window size
    @staticmethod
    def make_unique_window_sequences(window_size, arr, current_size, alphabet_array):
        if current_size == window_size:
            DebSequence.append_list_of_total_seq(arr, window_size)
            return totalNLengthSequences
        for number in alphabet_array:
            arr[current_size] = number
            DebSequence.make_unique_window_sequences(window_size, arr, current_size + 1, alphabet_array)

    # Gets what's created in this^. You need to separate it into 2 defs because ^ is iterative.
    @staticmethod
    def get_unique_window_sequences(m, a, c, aa):
        DebSequence.make_unique_window_sequences(m, a, c, aa)
        return totalNLengthSequences

    # this is the main iterative function that builds deb sequences. It starts with a base of any window-size string,
    # then looks for other window-length strings that have the same sequence as the first starting at the first's second
    # loci. For example, if string1 = "12345", string2 would be all strings that are 5 char long and start with "2345_"
    @staticmethod
    def build_on_this_base(growing_sequence, max_length, n_length_sequence_list, window_size,
                           already_used_sequences):
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
                        DebSequence.build_on_this_base(growing_sequence, max_length, n_length_sequence_list,
                                                       window_size, already_used_sequences)

    # from the (n, k) format, this takes n and turns it into an array [1, ..., n]
    # we'll use this to create the combinations of possible windows
    @staticmethod
    def make_alphabet_array(alpha_possibilities):
        alpha_options_array = []
        for i in range(alpha_possibilities):
            alpha_options_array.append(i)
        return alpha_options_array

    # this is the main function that pulls everything together. It takes in parameters in the (n, k) format
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


# this is just a practice one, showing how you'd test it out:)
print(DebSequence(2, 2))
