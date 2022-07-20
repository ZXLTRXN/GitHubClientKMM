//
//  AuthViewController.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 17.06.2022.
//

import UIKit
import MaterialComponents.MaterialActivityIndicator
import shared

class AuthViewController: UIViewController {
    
    @IBOutlet private weak var tokenTextField: UITextField!
    @IBOutlet private weak var errorLabel: UILabel!
    @IBOutlet private weak var signInButton: UIButton!
    @IBOutlet private weak var activityIndicator: MDCActivityIndicator!
    
    private let appRepo = DI.shared.appRepo
    
    private var editState: EditState = .valid
    private var validationEnabled = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tokenTextField.delegate = self
        setUI()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.setNavigationBarHidden(false, animated: animated)
    }
    
    private func setUI() {
        tokenTextField.placeholder = NSLocalizedString("auth.tokenTextField.placeholder", comment: "")
        tokenTextField.setBorderColor(UIColor(named: "TextFieldBorderColor")!.cgColor)
        
        signInButton.setTitle(NSLocalizedString("auth.signInButton.title", comment: ""), for: .normal)
        signInButton.setTitleColor(signInButton.backgroundColor, for: .disabled)
        activityIndicator.setColor()
    }
    
    private func onLoading(started flag: Bool) {
        tokenTextField.isEnabled = !flag
        signInButton.isEnabled = !flag
        if flag {
            activityIndicator.show()
        } else {
            activityIndicator.hide()
        }
    }
    
    @IBAction private func signInTapped(_ sender: UIButton) {
        if !validationEnabled {
            validationEnabled = true
            bindToEditState()
        }
        if editState == .valid {
            guard let token = tokenTextField.text else { return }
            onLoading(started: true)
            
            appRepo.signIn(token: token){ [weak self] error in
                self?.onLoading(started: false)
                guard let error = error else {
                     self?.navigationController?.setViewControllers([RepositoriesListViewController()], animated: true)
                    return
                }
                self?.showAlert(for: error.asCustomError(), sender: sender)
            }
        }
    }
    
    @IBAction private func editingChanged(_ sender: UITextField) {
        bindToEditState()
    }
    
    private func applyToTextField(errorMessage: String?, borderColor: UIColor) {
        tokenTextField.setBorderColor(borderColor.cgColor)
        errorLabel.isHidden = errorMessage == nil
        errorLabel.text = errorMessage
    }
    
    private func bindToEditState() {
        let errorMessage: String?
        let borderColor: UIColor
        if validationEnabled {
            editState = validateToken(tokenTextField.text)
            switch (editState) {
            case .invalid:
                errorMessage = NSLocalizedString("auth.inputErrorLabel.invalidToken.title", comment: "")
                borderColor = UIColor(named: "ErrorRed")!
            case .empty:
                errorMessage = NSLocalizedString("auth.inputErrorLabel.emptyToken.title", comment: "")
                borderColor = UIColor(named: "ErrorRed")!
            case .valid:
                errorMessage = nil
                borderColor = UIColor(named: "DefaultBlue")!
            }
        } else {
            errorMessage = nil
            borderColor = UIColor(named: "DefaultBlue")!
        }
        applyToTextField(errorMessage: errorMessage, borderColor: borderColor)
    }
    
    private func validateToken(_ token: String?) -> EditState {
        guard token?.isEmpty == false else { return .empty }
        let regularExpression = "[A-Za-z0-9_]{1,40}"
        let predicate = NSPredicate(format:"SELF MATCHES %@", regularExpression)
        if predicate.evaluate(with: token){
            return .valid
        } else {
            return .invalid
        }
    }
}

extension AuthViewController: UITextFieldDelegate {
    func textFieldDidBeginEditing(_ textField: UITextField) {
        bindToEditState()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        applyToTextField(errorMessage: nil, borderColor: UIColor(named: "TextFieldBorderColor")!)
    }
}

enum EditState: Equatable {
    case empty
    case invalid
    case valid
}
